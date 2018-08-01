package team.refer.mapper.referrer;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Haxor")
public class MappedIP {

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isWhiteListed() {
        return isWhiteListed;
    }

    public void setWhiteListed(boolean whiteListed) {
        isWhiteListed = whiteListed;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "MappedIP{" +
                "ip='" + ip + '\'' +
                ", country='" + country + '\'' +
                ", created=" + created +
                ", isWhiteListed=" + isWhiteListed +
                '}';
    }

    @Id
    @Column(name = "SourceIP")
    private String ip;
    private String country;
    private int count;
    @JsonFormat(pattern="yyyy-MM-dd@HH:mm")
    private Date created;
    @Column(name = "WhiteList")
    private boolean isWhiteListed;
}
