package team.refer.mapper.organization;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Service
public class OrganizationService {

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @RequestMapping(value = "/validateUniqueOrgKey")
    public boolean validateUniqueOrgKey(@RequestParam(value = "orgKey") String orgKey) {
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

    @RequestMapping(value = "/getOrg")
    public Organization getOrg(@RequestParam(value = "orgKey") String orgKey) {
        return organizationRepository.findByOrgKey(orgKey);
    }

    @RequestMapping(value = "/addOrg")
    public Organization addOrg(@RequestParam(value = "orgKey") String orgKey,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "url") String url,
                               @RequestParam(value = "default") boolean defaultOrg) {
        Organization org = new Organization();
        if (orgKey != null && !orgKey.equals("")) {
            org.setOrgKey(orgKey.toUpperCase());
            org.setName(name);
            org.setUrl(url);
            org.setDefault(defaultOrg);
            organizationRepository.save(org);
            if (defaultOrg) {
                Organization oldDefault = getDefaultOrganization();
                oldDefault.setDefault(false);
                organizationRepository.save(oldDefault);
            }
        }
        return org;
    }

    @RequestMapping(value = "/removeOrg")
    @Transactional
    public Organization removeOrg(@RequestParam(value = "orgKey") String orgKey) {
        Organization org = new Organization();
        org.setOrgKey(orgKey);
        organizationRepository.delete(org);
        getAllOrgs().remove(org);
        return org;
    }

    public Organization getDefaultOrganization() {
        return organizationRepository.findFirstByIsDefault(true);
    }

    private final OrganizationRepository organizationRepository;
}
