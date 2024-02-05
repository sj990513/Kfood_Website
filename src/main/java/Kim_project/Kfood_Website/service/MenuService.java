package Kim_project.Kfood_Website.service;


import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.domain.Recipe;
import Kim_project.Kfood_Website.repository.Menu.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuService {

    private MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    //전체메뉴 정렬
    public List<Menu> sortAllMenu() {
            return menuRepository.findAll();
    }

    //특정 멤버가 작성한 메뉴 정렬
    public List<Menu> sortAllMenuByMemberId(String memberId) {
        return menuRepository.findAllByMember(memberId);
    }

    //메뉴번호에 해당하는 메뉴
    public Optional<Menu> findMenuNumber(Long menuNumber) {
        return menuRepository.findByMenuNumber(menuNumber);
    }

    //메뉴재료별로 정렬
    public List<Menu> sortByNutrition(String nutrition) {
        return menuRepository.findMenuByNutrition(nutrition);
    }

    //메뉴검색어로 정렬
    public List<Menu> sortBySearch(String search) {
        return menuRepository.findByMenuName(search);
    }

    //메뉴 검색어별 정렬 + 사용자 아이디
    public List<Menu> sortBySearchAndMemberId(String search, String memberId) {
        return menuRepository.findByMenuNameAndMemberId(search, memberId);
    }

    //메뉴넘버에 해당되는 레세피 정렬
    public List<Recipe> sortRecipe(Long menuNumber) {
        return menuRepository.findRecipeByMenuName(menuNumber);
    }

    //메뉴추가
    public void addMenu(Menu menu) {
        menuRepository.saveMenu(menu);
    }

    //메뉴에 해당하는 레시피 추가
    public void addRecipe(String[] recipeDisc, String[] recipe_image) {

        //recipe 테이블의 외래키 참조값이 menu테이블의 menuNumber
        //테이블 설계시 db실습을 위해 menu테이블과 recipe테이블로 나눈 결과 조금 복잡하지만 다음과같이
        //recipe테이블 내에 menu테이블의 menuNumber를 삽입해 주어야 한다. 이부분 주의해야함
        List<Menu> lastMenu = menuRepository.findAll();
        Long menuNum = lastMenu.get(lastMenu.size()-1).getMenuNumber();
        

        for(int i=0; i<recipe_image.length; i++)
            menuRepository.saveRecipe(recipeDisc[i], recipe_image[i], menuNum);
    }

    //메뉴삭제
    public void deleteMenu(Long menuNumber) {
        menuRepository.deleteMenu(menuNumber);
    }

    //메뉴 업데이트
    public void updateMenu(String menuName, Long menuWeight, Long menuCal, Long menuCar, Long menuPro,
                           Long menuFat, Long menuNa, String menuImage, Long menuNumber) {
        menuRepository.updateMenu(menuName, menuWeight, menuCal, menuCar, menuPro, menuFat, menuNa, menuImage, menuNumber);

    }

    //레시피 업데이트
    public void updateRecipe(String[] recipes, String[] recipeImages, Long menuNumber) {

        List<Recipe> before = menuRepository.findRecipeByMenuName(menuNumber);

        //기존보다 레시피 수 삭제해서 들어올떄
        if (before.size() > recipes.length) {

            int diff = before.size() - recipes.length;

            //차이나는 수만큼 삭제
            for (int i = before.size(); i > before.size()- diff; i--)
                menuRepository.deleteRecipe(before.get(i-1).getReipeNumber());

            for(int i=0; i<recipes.length; i++)
                menuRepository.updateRecipe(recipes[i], recipeImages[i], menuNumber, before.get(i).getReipeNumber());
        }


        //기존보다 레시피 수  추가해서 들어올떄
        else if (before.size() < recipes.length) {

            int diff = recipes.length - before.size();

            // First, update the original length
            for (int i = 0; i < Math.min(before.size(), recipes.length); i++)
                menuRepository.updateRecipe(recipes[i], recipeImages[i], menuNumber, before.get(i).getReipeNumber());

            // From then on, additionally
            for (int i = before.size(); i < recipes.length; i++)
                menuRepository.saveRecipe(recipes[i], recipeImages[i], menuNumber);
        }

        //기존거랑 똑같을때
        else {
            for(int i=0; i<recipes.length; i++)
                menuRepository.updateRecipe(recipes[i], recipeImages[i], menuNumber, before.get(i).getReipeNumber());
        }
    }

    //메뉴(메뉴넘버)에 대한 댓글 조회
    public List<Comment> findCommentByMenuNumber(Long menuNumber) {
        return menuRepository.findAllByMenu(menuNumber);
    }

    //특정 메뉴넘버에 로그인중인 사용자의 댓글 추가
    public void addComment(String commentText, Long menuNumber, String memberId) {
        menuRepository.saveComment(commentText, menuNumber, memberId);
    }

    //댓글 삭제
    public void deleteComment(Long commentNumber) {
        menuRepository.deleteComment(commentNumber);
    }

    //특정 멤버가 작성한 댓글 정렬, memberService에 있는게 좋을듯하다.
    public List<Comment> findCommentByMemberId(String memberId) {
        return menuRepository.findByMemberId(memberId);
    }
}
