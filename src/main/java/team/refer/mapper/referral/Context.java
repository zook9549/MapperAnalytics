package team.refer.mapper.referral;

import com.fasterxml.jackson.annotation.JsonFormat;
import team.refer.mapper.application.Application;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ReferralHistory")
@IdClass(ContextKey.class)
public class Context {

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getReferringURL() {
        return referringURL;
    }

    public void setReferringURL(String referringURL) {
        this.referringURL = referringURL;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public Map<String, String> getAdditionalParam() {
        return additionalParam;
    }

    public void setAdditionalParam(Map<String, String> additionalParam) {
        this.additionalParam = additionalParam;
    }

    public String getResultURL() {
        return resultURL;
    }

    public void setResultURL(String resultURL) {
        this.resultURL = resultURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getMappedTimeStamp() {
        return mappedTimeStamp;
    }

    public void setMappedTimeStamp(Date mappedTimeStamp) {
        this.mappedTimeStamp = mappedTimeStamp;
    }

    public long getMappedMilliseconds() {
        return mappedMilliseconds;
    }

    public void setMappedMilliseconds(long mappedMilliseconds) {
        this.mappedMilliseconds = mappedMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return mappedMilliseconds == context.mappedMilliseconds &&
                Objects.equals(application, context.application);
    }

    @Override
    public int hashCode() {

        return Objects.hash(application, mappedMilliseconds);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AppKey")
    @Id
    private Application application;
    @Id
    private long mappedMilliseconds;
    private String referringURL;
    private String originalURL;
    private String resultURL;
    private String userName;
    private String sourceIP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm",timezone = "GMT-04:00")
    private Date mappedTimeStamp;
    @javax.persistence.Transient
    private Map<String, String> additionalParam = new HashMap<>();
}
