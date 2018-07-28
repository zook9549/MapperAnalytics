package team.refer.mapper;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.refer.mapper.application.Application;
import team.refer.mapper.application.ApplicationService;
import team.refer.mapper.organization.Organization;
import team.refer.mapper.organization.OrganizationService;
import team.refer.mapper.referral.Context;
import team.refer.mapper.referral.ReferralHistoryService;
import team.refer.mapper.referrer.SourceValidationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@EnableAutoConfiguration
@EnableCaching
@RestController
@SpringBootApplication
@EnableEncryptableProperties
@PropertySource("classpath:application.properties")
@RequestMapping(value = "/")
public class MapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapperApplication.class, args);
    }


    @RequestMapping(value = "/generateVanityURL")
    public String generateVanityURL(HttpServletRequest request, @RequestParam(value = "email") String email, @RequestParam(value = "app", required = false) String appName) {
        Application application;
        if (appName == null) {
            application = applicationService.getDefaultApplication();
        } else {
            application = applicationService.findBestMatch(appName);
        }
        StringBuilder url = new StringBuilder(getCallBackURL(application, request)).append('/');

        if (!application.isDefaultApp()) {
            url.append(application.getAppKey()).append('/');
        }
        String shortenedEmail = email.toLowerCase().replace("@" + application.getEmailDefaultDomain().toLowerCase(), "");
        url.append(shortenedEmail);
        return url.toString().toLowerCase();
    }

    @RequestMapping(value = "/{path:(?:admin|scripts|styles|fonts)}/{fileName:.*}")
    public byte[] proxy(HttpServletResponse response, @PathVariable String path, @PathVariable String fileName) throws IOException {
        String filePath = path + '/' + fileName;
        try {
            File file = ResourceUtils.getFile("classpath:static/" + filePath);
            return Files.readAllBytes(file.toPath());
        } catch (FileNotFoundException ex) {
            LOG.debug("Request made for non-existent file " + filePath, ex);
            response.setStatus(404);
            return null;
        }
    }

    @RequestMapping(value = "/**")
    @Transactional
    public void getURLValue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Application application;
        String mappedURL;
        String user = "";
        ArrayList<String> mappedParams = new ArrayList<>();
        mappedParams.add(request.getRequestURI());
        String[] params = request.getRequestURI().split("/");

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (sourceValidationService.isValidSource(ipAddress, request.getRequestURL().toString())) {
            if (params.length > 1) {
                application = applicationService.getApplicationByKey(params[1]);
                int startIndex = 1;
                if (application == null) {
                    application = applicationService.getDefaultApplication(findOrganization(request));
                } else {
                    startIndex = 2;
                }
                if (startIndex < params.length) {
                    user = getUserParam(application, params[startIndex]);
                }
                mappedParams.add(application.getAppKey());
                mappedParams.add(user);
                mappedParams.addAll(Arrays.asList(params).subList(startIndex + 1, params.length));
                mappedURL = MessageFormat.format(application.getUrl(), mappedParams.toArray());
                Context context = new Context();
                context.setApplication(application);
                context.setUserName(user);
                context.setReferringURL(request.getHeader(HttpHeaders.REFERER));
                context.setOriginalURL(request.getRequestURL().toString());
                context.setResultURL(mappedURL);
                context.setSourceIP(ipAddress);
                context.setMappedMilliseconds(new Date().getTime());
                referralHistoryService.storeReferrer(context);
            } else {
                //check for org in URL, otherwise default org
                mappedURL = findOrganization(request).getUrl();
            }
            LOG.info("Routing to " + mappedURL);
            response.sendRedirect(mappedURL);
        } else {
            LOG.info("Haxor attempt by " + ipAddress + " for URL " + request.getRequestURL());
            response.sendError(407);
        }
    }


    private Organization findOrganization(HttpServletRequest request) {
        String domain = request.getServerName();
        String[] domainParts = domain.split("\\.");
        if (domainParts.length == 3) {
            String orgName = domainParts[0];
            for (Organization org : organizationService.getAllOrgs()) {
                if (org.getOrgKey().equalsIgnoreCase(orgName)) {
                    return org;
                }
            }
        }
        return organizationService.getDefaultOrganization();
    }


    private String getUserParam(@NotNull Application app, String user) {
        String emailDomain = app.getEmailDefaultDomain();
        String userParam = user;
        if (user != null && user.length() > 0) {
            if (emailDomain != null) {
                if (user.indexOf('@') < 0) {
                    userParam = user + '@' + emailDomain;
                }
            }
        }
        return userParam;
    }

    private String getCallBackURL(Application application, HttpServletRequest request) {
        String baseDomain = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        Organization org = application.getOrganization();
        String domain = request.getServerName();
        String[] domainParts = domain.split("\\.");
        if (domainParts.length >= 2) {
            StringBuilder optimizedDomain = new StringBuilder();
            if (!org.isDefault()) {
                optimizedDomain.append(org.getOrgKey()).append('.');
            }
            for (int i = domainParts.length - 2; i < domainParts.length; i++) {
                optimizedDomain.append(domainParts[i]);
                if (i < domainParts.length - 1) {
                    optimizedDomain.append('.');
                }
            }
            baseDomain = baseDomain.replace(domain, optimizedDomain.toString());
        } else {
            LOG.info("Ensure you are running in a non-domain dev env. Non Default org not mapped to a proper subdomain: " + org.getOrgKey());
        }
        return baseDomain;
    }

    @Autowired
    private SourceValidationService sourceValidationService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ReferralHistoryService referralHistoryService;

    private static final Logger LOG = LoggerFactory.getLogger(MapperApplication.class);

}
