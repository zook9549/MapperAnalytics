package team.refer.mapper.organization;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Service
public class OrganizationService {

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @RequestMapping(value = "/validateUniqueOrgKey")
    public boolean validateUniqueOrgKey(@RequestParam(value = "orgKey", required = true) String orgKey) {
        for (Organization org : getAllOrgs()) {
            if (org.getOrgKey().equalsIgnoreCase(orgKey)) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/getAllOrgs")
    public List<Organization> getAllOrgs() {
        return (List) organizationRepository.findAll();
    }

    @RequestMapping(value = "/addOrg")
    public Organization addOrg(@RequestParam(value = "orgKey") String orgKey, @RequestParam(value = "name") String name, @RequestParam(value = "url") String url) {
        Organization org = new Organization();
        if (validateUniqueOrgKey(orgKey)) {
            org.setOrgKey(orgKey.toUpperCase());
            org.setName(name);
            org.setUrl(url);
            organizationRepository.save(org);
        }
        return org;
    }

    @RequestMapping(value = "/removeOrg")
    public Organization removeOrg(@RequestParam(value = "orgKey") String orgKey) {
        Organization org = new Organization();
        org.setOrgKey(orgKey);
        organizationRepository.delete(org);
        getAllOrgs().remove(org);
        return org;
    }

    public Organization getOrganizationByKey(String key) {
        return organizationRepository.findByOrgKey(key);
    }

    // todo just get rid of this
    public Organization getDefaultOrganization() {
        return getOrganizationByKey(defaultOrg);
    }


    private final OrganizationRepository organizationRepository;
    @Value("${org.default}")
    private String defaultOrg;
}
