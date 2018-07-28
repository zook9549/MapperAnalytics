package team.refer.mapper.organization;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = Organization.TABLE_NAME)
public class Organization {

    public static final String TABLE_NAME = "Organization";
    public Organization() {
    }

    public String getOrgKey() {
        return orgKey;
    }

    public void setOrgKey(String orgKey) {
        this.orgKey = orgKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(orgKey, that.orgKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orgKey);
    }

    @Id
    private String orgKey;
    private String name;
    private String url;
    @Column(name = "OwnerEmail")
    private String owner;
    private boolean isDefault;
}
