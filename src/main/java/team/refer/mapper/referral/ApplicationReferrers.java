package team.refer.mapper.referral;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ApplicationReferrers {

    public ApplicationReferrers() {
    }

    public Date getReferDate() {
        return referDate;
    }

    public void setReferDate(Date referDate) {
        this.referDate = referDate;
    }

    public List<GroupReferralCount> getGroupReferralCounts() {
        return groupReferralCounts;
    }

    public void setGroupReferralCounts(List<GroupReferralCount> groupReferralCounts) {
        this.groupReferralCounts = groupReferralCounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationReferrers that = (ApplicationReferrers) o;
        return Objects.equals(referDate, that.referDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(referDate);
    }

    @JsonFormat(pattern="yyyy-MM-dd@HH:mm")
    private Date referDate;
    private List<GroupReferralCount> groupReferralCounts;
}
