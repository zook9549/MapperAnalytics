package team.refer.mapper.referrer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        return (List) mappedIPRepository.findAllByOrderByCreatedDesc();
    }

    @RequestMapping(value = "/toggleWhiteList")
    public MappedIP toggleWhiteList(@RequestParam(value = "ip") String ip) {
        MappedIP mappedIP = mappedIPRepository.findByIp(ip);
        mappedIP.setWhiteListed(!mappedIP.isWhiteListed());
        mappedIPRepository.save(mappedIP);
        return mappedIP;
    }

    public boolean isValidSource(String ipAddress, String uri) {
        boolean malicious = uri.toLowerCase().matches(haxorMatch);
        MappedIP result = mappedIPRepository.findByIp(ipAddress);
        if (result == null && malicious) {
            result = mapIPAddress(ipAddress);
            LOG.warn("Malicious attempt: " + uri + " : " + result);
            mappedIPRepository.save(result);
        } else if (result != null) {
            if (malicious) {
                result.setCount(result.getCount() + 1);
                mappedIPRepository.save(result);
                if (result.isWhiteListed() && malicious) {
                    LOG.warn("Overriding since IP is whitelisted: " + uri + " : " + result);
                }
            }
            malicious = !result.isWhiteListed();
        }
        return !malicious;
    }

    private MappedIP mapIPAddress(String ipAddress) {
        MappedIP result = new MappedIP();
        result.setCreated(new Date());
        result.setIp(ipAddress);
        result.setWhiteListed(false);
        result.setCountry(mapCountry(ipAddress));
        result.setCount(1);
        return result;
    }

    private String mapCountry(String ipAddress) {
        String url = MessageFormat.format(apiURL, ipAddress);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> results = restTemplate.getForEntity(url, Map.class);
        return (String) results.getBody().get("country_code");
    }

    private final MappedIPRepository mappedIPRepository;

    @Value("${ip.api.lookup.url}")
    private String apiURL;
    @Value("${haxor.match}")
    private String haxorMatch;

    static final Logger LOG = LoggerFactory.getLogger(SourceValidationService.class);
}
