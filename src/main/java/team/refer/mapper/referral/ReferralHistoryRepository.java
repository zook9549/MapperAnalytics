package team.refer.mapper.referral;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.refer.mapper.application.Application;

import java.util.Collection;

@Repository
public interface ReferralHistoryRepository extends CrudRepository<Context, String> {

    Collection<Context> findByApplication(Application application);

    @Override
    void delete(Context context);
}
