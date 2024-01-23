package Kim_project.Kfood_Website.repository.Menu;

import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.domain.Recipe;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {

    //메뉴번호로 메뉴찾기
    Optional<Menu> findByMenuNumber(Long menuNumber);

    //모든메뉴 정렬
    List<Menu> findAll();

    //특정 멤버가 작성한 모든메뉴 정렬
    List<Menu> findAllByMember(String memberId);

    //재료별로 메뉴정렬
    List<Menu> findMenuByNutrition(String nutrition);

    //메뉴 이름으로 메뉴정렬
    List<Menu> findByMenuName(String name);

    //특정 멤버아이디와 메뉴이름으로 정렬
    List<Menu> findByMenuNameAndMemberId(String name, String memberId);

    //메뉴 이름과 메뉴넘버로 정렬
    List<Menu> findByMenuNameAndMenuNumber(String name, Long menuNumber);
    
    //메뉴 넘버로 레시피 정렬
    List<Recipe> findRecipeByMenuName(Long menuNumber);
    
    //메뉴저장
    void saveMenu(Menu menu);
    
    //레시피 저장
    void saveRecipe(String recipeDisc, String recipeImgUrl, Long menuNumber);
    
    //메뉴삭제
    void deleteMenu(Long menuNumber);
    
    //메뉴 업데이트
    void updateMenu (String menuName, Long menuWeight, Long menuCal, Long menuCar, Long menuPro, Long menuFat, Long menuNa, String menuImage, Long menuNumber);
    
    //레시피 업데이트
    void updateRecipe (String recipes, String recipeImages, Long menuNumber, Long recipeNumber);
    
    //레시피 삭제
    void deleteRecipe (Long recipeNumber);
    
    //특정 메뉴의 모든 댓글 정렬
    List<Comment> findAllByMenu(Long menuNumber);
    
    //댓글저장
    void saveComment(String commentText, Long menuNumber, String memberId);
    
    //댓글삭제
    void deleteComment(Long commentNumber);
    
    //특정 멤버아이디의 댓글정렬
    List<Comment> findByMemberId(String memberId);
}
