package Kim_project.Kfood_Website.convertDTO;

import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Recipe;
import Kim_project.Kfood_Website.dto.CommentDTO;
import Kim_project.Kfood_Website.dto.RecipeDTO;

import java.util.ArrayList;
import java.util.List;

public class ConvertToRecipeDTO {

    public List<RecipeDTO> convertToRecipeDTO(List<Recipe> recipes) {
        List<RecipeDTO> recipeDTOS = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setRecipeDisc(recipe.getRecipeDisc());
            recipeDTO.setRecipeImageUrl(recipe.getRecipeImageUrl());
            recipeDTO.setMenuNumber(recipe.getMenuNumber());
            recipeDTO.setReipeNumber(recipe.getReipeNumber());
            recipeDTOS.add(recipeDTO);
        }
        return recipeDTOS;
    }
}
