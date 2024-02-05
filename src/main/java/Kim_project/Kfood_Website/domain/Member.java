package Kim_project.Kfood_Website.domain;


public class Member {

    private String memberId;
    private String memberPassword;
    private String memberPhoneNumber;
    private String memberName;
    private String memberAuthority;
    private String memberEmail;


    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberAuthority() {
        return memberAuthority;
    }

    public void setMemberAuthority(String memberAuthority) {
        this.memberAuthority = memberAuthority;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memeberName) {
        this.memberName = memeberName;
    }
}
