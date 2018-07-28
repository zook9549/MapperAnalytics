package team.refer.mapper.organization;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableCaching
public interface OrganizationRepository extends CrudRepository<Organization, String> {

    @Cacheable(value = "orgs")
    Organization findByOrgKey(String orgKey);

    @Override
    @CacheEvict(value = "orgs", allEntries = true)
    <S extends Organization> S save(S s);

    @Override
    @Cacheable(value = "orgs")
    Iterable<Organization> findAll();

    @Override
    @CacheEvict(value = "orgs", allEntries = true)
    void delete(Organization organization);
}
