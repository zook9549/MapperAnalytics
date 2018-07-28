package team.refer.mapper.referral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.refer.mapper.application.Application;
import team.refer.mapper.application.ApplicationRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@Service
public class ReferralHistoryService {


    @Autowired
    public ReferralHistoryService(ReferralHistoryRepository referralHistoryRepository, ApplicationRepository applicationRepository, JdbcTemplate jdbcTemplate) {
        this.referralHistoryRepository = referralHistoryRepository;
        this.applicationRepository = applicationRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping(value = "/getReferralsByAppKey")
    public Collection<Context> getReferralsByAppKey(@RequestParam(value = "appKey", required = true) String appKey) {
        return referralHistoryRepository.findByApplication(applicationRepository.findByAppKey(appKey));
    }

    @RequestMapping(value = "/getTopReferrers")
    public List<GroupReferralCount> getTopReferrers(@RequestParam(value = "maxRecords", defaultValue = "10") int maxRecords) {
        SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT count(1) as referrals, username FROM MapperAnalytics.ReferralHistory group by username order by referrals desc");
        ArrayList<GroupReferralCount> results = new ArrayList<>();
        for (int i = 0; i < maxRecords && srs.next(); i++) {
            results.add(new GroupReferralCount(srs.getString("username"), srs.getInt("referrals"), null));
        }
        return results;
    }

    @RequestMapping(value = "/getTopReferrersByIPAddress")
    public List<GroupReferralCount> getTopReferrersByIPAddress(@RequestParam(value = "maxRecords", defaultValue = "10") int maxRecords) {
        SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT count(1) as referrals, sourceIP FROM MapperAnalytics.ReferralHistory group by sourceIP order by referrals desc");
        ArrayList<GroupReferralCount> results = new ArrayList<>();
        for (int i = 0; i < maxRecords && srs.next(); i++) {
            results.add(new GroupReferralCount(srs.getString("sourceIP"), srs.getInt("referrals"), null));
        }
        return results;
    }

    @RequestMapping(value = "/getReferrersByApp")
    public Collection<ApplicationReferrers> getReferrersByApp(@RequestParam(value = "groupByDay", defaultValue = "true") boolean groupByDay,
                                                              @RequestParam(value = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start)
            throws Exception {
        if (start == null) {
            start = LocalDate.now();
        }
        String groupByFormat;
        DateFormat df;
        if (groupByDay) {
            groupByFormat = "%Y-%m-%d";
            df = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            groupByFormat = "%Y-%m-%d  %H:%i";
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
        SqlRowSet srs = jdbcTemplate.queryForRowSet("SELECT AppKey, count(1) as Referrals, DATE_FORMAT(MappedTimeStamp,'" + groupByFormat + "') as ReferralDate FROM ReferralHistory " +
                "WHERE MappedTimeStamp >= ? GROUP BY ReferralDate, AppKey ORDER BY ReferralDate, AppKey, Referrals DESC", java.sql.Date.valueOf(start));
        ArrayList<ApplicationReferrers> applicationReferrers = new ArrayList<>();
        while (srs.next()) {
            String appKey = srs.getString("AppKey");
            Date referralDate = df.parse(srs.getString("ReferralDate"));
            int referralCount = srs.getInt("Referrals");
            ApplicationReferrers newDateReferrer = new ApplicationReferrers();
            newDateReferrer.setReferDate(referralDate);
            if (applicationReferrers.contains(newDateReferrer)) {
                newDateReferrer = applicationReferrers.get(applicationReferrers.indexOf(newDateReferrer));
            } else {
                applicationReferrers.add(newDateReferrer);
            }
            List<GroupReferralCount> results = newDateReferrer.getGroupReferralCounts();
            if (results == null) {
                results = new ArrayList<>();
                newDateReferrer.setGroupReferralCounts(results);
            }
            results.add(new GroupReferralCount(appKey, referralCount, referralDate));
        }
        List<Application> apps = (List)applicationRepository.findAll();
        for (ApplicationReferrers applicationReferrer : applicationReferrers) {
            Date date = applicationReferrer.getReferDate();
            ArrayList<Application> workingApps = new ArrayList<>(apps);
            List<GroupReferralCount> results = applicationReferrer.getGroupReferralCounts();
            for (GroupReferralCount result : results) {
                Application app = new Application();
                app.setAppKey(result.getIdentifierGroup());
                workingApps.remove(app);
            }
            for (Application workingApp : workingApps) {
                GroupReferralCount emptyCount = new GroupReferralCount(workingApp.getAppKey(), 0, date);
                results.add(emptyCount);
            }
        }
        return applicationReferrers;
    }


    @RequestMapping(value = "/removeReferral")
    @Transactional
    public Context removeReferral(@RequestParam(value = "appKey") String appKey, @RequestParam(value = "mappedMilliseconds") long millis) {
        Application app = applicationRepository.findByAppKey(appKey);
        Context referralHistory = new Context();
        referralHistory.setMappedMilliseconds(millis);
        referralHistory.setApplication(app);
        referralHistoryRepository.delete(referralHistory);
        return referralHistory;
    }

    //todo move this repo
    public void storeReferrer(Context context) {
        jdbcTemplate.update("INSERT INTO ReferralHistory(AppKey, MappedMilliseconds, MappedTimeStamp, OriginalURL, ReferringURL, ResultURL, UserName, SourceIP) VALUES (?,?,?,?,?,?,?,?)",
                context.getApplication().getAppKey(), context.getMappedMilliseconds(), new Timestamp(context.getMappedMilliseconds()),
                context.getOriginalURL(), context.getReferringURL(), context.getResultURL(), context.getUserName(), context.getSourceIP());
    }

    private final ReferralHistoryRepository referralHistoryRepository;
    private final ApplicationRepository applicationRepository;
    final JdbcTemplate jdbcTemplate;
}
