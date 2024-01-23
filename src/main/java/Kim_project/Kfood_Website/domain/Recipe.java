package Kim_project.Kfood_Website.domain;

public class Recipe {

    private Long reipeNumber;
    private String recipeDisc;
    private String recipeImageUrl;
    private Long menuNumber;

    public Long getReipeNumber() {
        return reipeNumber;
    }

    public void setReipeNumber(Long reipeNumber) {
        this.reipeNumber = reipeNumber;
    }

    public String getRecipeDisc() {
        return recipeDisc;
    }

    public void setRecipeDisc(String recipeDisc) {
        this.recipeDisc = recipeDisc;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    public Long getMenuNumber() {
        return menuNumber;
    }

    public void setMenuNumber(Long menuNumber) {
        this.menuNumber = menuNumber;
    }
}
