package Kim_project.Kfood_Website.repository.Menu;

import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.domain.Recipe;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRowMapper implements RowMapper<Recipe> {

    @Override
    public Recipe mapRow(ResultSet rs, int rowNum) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setReipeNumber(rs.getLong("recipeNumber"));
        recipe.setRecipeDisc(rs.getString("recipeDisc"));
        recipe.setRecipeImageUrl(rs.getString("recipeImageUrl"));
        recipe.setMenuNumber(rs.getLong("menuNumber"));
        return recipe;
    }
}
