package team.refer.mapper.referrer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@Service
@PropertySource("classpath:application.properties")
public class SourceValidationService {


    @Autowired
    public SourceValidationService(MappedIPRepository mappedIPRepository) {
        this.mappedIPRepository = mappedIPRepository;
    }

    @RequestMapping(value = "/getHaxors")
    public Collection<MappedIP> getHaxors() {
        return (List) mappedIPRepository.findAll();
    }

    @RequestMapping(value = "/toggleWhiteList")
    public MappedIP toggleWhiteList(@RequestParam(value = "ip", required = true) String ip) {
        MappedIP mappedIP = mappedIPRepository.findByIp(ip);
        mappedIP.setWhiteListed(!mappedIP.isWhiteListed());
        mappedIPRepository.save(mappedIP);
        return mappedIP;
    }

    public boolean isValidSource(String ipAddress, String uri) {
        boolean malicious = uri.toLowerCase().matches(haxorMatch);
        MappedIP result = mappedIPRepository.findByIp(ipAddress);
        if (result == null && malicious) {
            result = new MappedIP();
            result.setCreated(new Date());
            result.setIp(ipAddress);
            result.setWhiteListed(false);
            LOG.warn("Malicious attempt: " + uri + " : " + result);
            mappedIPRepository.save(result);
        } else if (result != null) {
            if (result.isWhiteListed() && malicious) {
                LOG.warn("Overriding since IP is whitelisted: " + uri + " : " + result);
            }
            malicious = !result.isWhiteListed();
        }
        return !malicious;
    }

    private final MappedIPRepository mappedIPRepository;

    @Value("${blacklist.api.url}")
    private String apiURL;
    @Value("${haxor.match}")
    private String haxorMatch;

    static final Logger LOG = LoggerFactory.getLogger(SourceValidationService.class);
}
