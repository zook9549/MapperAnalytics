package team.refer.mapper.referrer;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableCaching
@Repository
public interface MappedIPRepository extends CrudRepository<MappedIP, String> {

    @Cacheable(value = "mappedIPs")
    MappedIP findByIp(String ipAddress);

    @Override
    @Cacheable(value = "mappedIPs")
    Optional<MappedIP> findById(String s);

    @Override
    @Cacheable(value = "mappedIPs")
    Iterable<MappedIP> findAll();

    @Override
    @CacheEvict(value = "mappedIPs", allEntries = true)
    <S extends MappedIP> S save(S s);
}
