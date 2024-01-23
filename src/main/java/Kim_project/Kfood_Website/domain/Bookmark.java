package Kim_project.Kfood_Website.domain;

public class Bookmark {
    private Long menuNumber;
    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Long getMenuNumber() {
        return menuNumber;
    }

    public void setMenuNumber(Long menuNumber) {
        this.menuNumber = menuNumber;
    }
}
