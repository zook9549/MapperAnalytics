package team.refer.mapper.referral;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class GroupReferralCount {
    public GroupReferralCount(String identifierGroup, int count, Date referralDate) {
        this.identifierGroup = identifierGroup;
        this.count = count;
        this.referralDate = referralDate;
    }

    public String getIdentifierGroup() {
        return identifierGroup;
    }

    public void setIdentifierGroup(String identifierGroup) {
        this.identifierGroup = identifierGroup;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(Date referralDate) {
        this.referralDate = referralDate;
    }

    private String identifierGroup;
    private int count;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date referralDate;
}
