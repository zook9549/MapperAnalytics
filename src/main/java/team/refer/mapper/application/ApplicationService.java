package team.refer.mapper.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.refer.mapper.organization.Organization;
import team.refer.mapper.organization.OrganizationService;
import team.refer.mapper.referral.ReferralHistoryRepository;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Service
public class ApplicationService {

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, OrganizationService organizationService, ReferralHistoryRepository referralHistoryRepository) {
        this.applicationRepository = applicationRepository;
        this.organizationService = organizationService;
        this.referralHistoryRepository = referralHistoryRepository;
    }

    @RequestMapping(value = "/validateUniqueAppKey")
    public boolean validateUniqueAppKey(@RequestParam(value = "appKey") String appKey) {
        for (Application app : getAllApps()) {
            if (app.getAppKey().equalsIgnoreCase(appKey)) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/getAllApps")
    public List<Application> getAllApps() {
        return (List) applicationRepository.findAll();
    }

    @RequestMapping(value = "/removeApp")
    @Transactional
    public Application removeApp(@RequestParam(value = "appKey") String appKey) {
        Application app = applicationRepository.findByAppKey(appKey);
        referralHistoryRepository.deleteAll(app.getReferralHistory());
        applicationRepository.delete(app);
        return app;
    }

    @RequestMapping(value = "/addApp")
    public Application addOrg(@RequestParam(value = "appKey") String appKey,
                              @RequestParam(value = "orgKey") String orgKey,
                              @RequestParam(value = "description") String description,
                              @RequestParam(value = "url") String url,
                              @RequestParam(value = "emailDefaultDomain") String emailDefaultDomain,
                              @RequestParam(value = "defaultApp") boolean defaultApp) {
        Application app = new Application();
        Organization org = organizationService.getOrg(orgKey);
        if (org != null) {
            app.setAppKey(appKey);
            app.setDescription(description);
            app.setUrl(url);
            app.setDefaultApp(defaultApp);
            app.setEmailDefaultDomain(emailDefaultDomain);
            app.setOrganization(org);
            applicationRepository.save(app);
        }
        return app;
    }

    public Application findBestMatch(String appName) {
        Application application = applicationRepository.findByAppKey(appName);
        if (application == null) {
            application = getApplicationByDescription(appName);
        }
        if (application == null) {
            LOG.info("Falling back to default application since a match could not be found for " + appName);
            application = getDefaultApplication();
        }
        return application;
    }

    private Application getApplicationByDescription(String description) {
        for (Application application : getAllApps()) {
            if (application.getDescription().equalsIgnoreCase(description)) {
                return application;
            }
        }
        return null;
    }

    public Application getApplicationByKey(String appKey) {
        return applicationRepository.findByAppKey(appKey);
    }

    public Application getDefaultApplication() {
        Organization org = organizationService.getDefaultOrganization();
        return getDefaultApplication(org);
    }

    public Application getDefaultApplication(Organization org) {
        return applicationRepository.findFirstByIsDefaultAppAndOrganization(true, org);
    }

    private final OrganizationService organizationService;
    private final ApplicationRepository applicationRepository;
    private final ReferralHistoryRepository referralHistoryRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationService.class);
}
