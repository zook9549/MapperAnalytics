package team.refer.mapper.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import team.refer.mapper.referral.Context;
import team.refer.mapper.organization.Organization;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "Application")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Application implements Serializable {

    public Application() {
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmailDefaultDomain() {
        return emailDefaultDomain;
    }

    public void setEmailDefaultDomain(String emailDefaultDomain) {
        this.emailDefaultDomain = emailDefaultDomain;
    }

    public boolean isDefaultApp() {
        return isDefaultApp;
    }

    public void setDefaultApp(boolean defaultApp) {
        isDefaultApp = defaultApp;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Collection<Context> getReferralHistory() {
        return referralHistory;
    }

    public void setReferralHistory(Collection<Context> referralHistory) {
        this.referralHistory = referralHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(appKey, that.appKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(appKey);
    }

    @Id
    private String appKey;
    private String description;
    private String url;
    private String emailDefaultDomain;
    private boolean isDefaultApp;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OrgKey")
    private Organization organization;

    @OneToMany(
            mappedBy = "application"
    )
    @JsonIgnore
    private Collection<Context> referralHistory;
}
