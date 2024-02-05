package Kim_project.Kfood_Website.dto;

public class MenuDTO {
    private Long menuNumber;
    private String menuName;
    private Long menuWeight;
    private Long menuCal;
    private Long menuCar;
    private Long menuPro;
    private Long menuFat;
    private Long menuNa;
    private String menuImageUrl;
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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getMenuWeight() {
        return menuWeight;
    }

    public void setMenuWeight(Long menuWeight) {
        this.menuWeight = menuWeight;
    }

    public Long getMenuCal() {
        return menuCal;
    }

    public void setMenuCal(Long menuCal) {
        this.menuCal = menuCal;
    }

    public Long getMenuCar() {
        return menuCar;
    }

    public void setMenuCar(Long menuCar) {
        this.menuCar = menuCar;
    }

    public Long getMenuPro() {
        return menuPro;
    }

    public void setMenuPro(Long menuPro) {
        this.menuPro = menuPro;
    }

    public Long getMenuFat() {
        return menuFat;
    }

    public void setMenuFat(Long menuFat) {
        this.menuFat = menuFat;
    }

    public Long getMenuNa() {
        return menuNa;
    }

    public void setMenuNa(Long menuNa) {
        this.menuNa = menuNa;
    }

    public String getMenuImageUrl() {
        return menuImageUrl;
    }

    public void setMenuImageUrl(String menuImageUrl) {
        this.menuImageUrl = menuImageUrl;
    }
}
