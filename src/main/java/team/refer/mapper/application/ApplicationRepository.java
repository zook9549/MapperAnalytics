package team.refer.mapper.application;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.refer.mapper.organization.Organization;

@Repository
@EnableCaching
public interface ApplicationRepository extends CrudRepository<Application, String> {

    @Cacheable(value = "apps")
    Application findByAppKey(String appKey);

    @Cacheable(value = "apps")
    int countApplicationByOrganization(Organization organization);

    @Cacheable(value = "apps")
    Application findFirstByIsDefaultAppAndOrganization(boolean isDefaultApp, Organization organization);

    @CacheEvict(value = "apps", allEntries = true)
    void delete(Application application);

    @Override
    @CacheEvict(value = "apps", allEntries = true)
    <S extends Application> S save(S s);

    @Override
    @Cacheable(value = "apps")
    Iterable<Application> findAll();
}
