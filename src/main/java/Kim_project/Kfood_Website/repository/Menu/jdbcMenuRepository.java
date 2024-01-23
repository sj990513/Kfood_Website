package Kim_project.Kfood_Website.repository.Menu;


import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class jdbcMenuRepository implements MenuRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public jdbcMenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //메뉴번호로 메뉴찾기
    @Override
    public Optional<Menu> findByMenuNumber(Long menuNumber) {
        String sql = "SELECT * FROM menu WHERE menuNumber = ?";
        List<Menu> menus = jdbcTemplate.query(sql, new Object[]{menuNumber}, new MenuRowMapper());
        return menus.stream().findAny();
    }

    //모든메뉴 정렬
    @Override
    public List<Menu> findAll() {
        String sql = "SELECT * FROM MENU";
        return jdbcTemplate.query(sql, new MenuRowMapper());
    }

    //특정 멤버가 작성한 모든메뉴 정렬
    @Override
    public List<Menu> findAllByMember(String memberId) {
        String sql = "SELECT * FROM MENU WHERE memberId = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, new MenuRowMapper());
    }

    //재료별로 메뉴정렬
    @Override
    public List<Menu> findMenuByNutrition(String nutrition) {
        String sql = "SELECT * FROM MENU ORDER BY " + nutrition + " ASC";
        return jdbcTemplate.query(sql, new MenuRowMapper());
    }

    //메뉴 이름으로 메뉴정렬
    @Override
    public List<Menu> findByMenuName(String name) {
        String sql = "SELECT * FROM menu WHERE menuname LIKE '%" + name + "%'";
        return jdbcTemplate.query(sql, new MenuRowMapper());
    }

    //특정 멤버아이디와 메뉴이름으로 정렬
    @Override
    public List<Menu> findByMenuNameAndMemberId(String name, String memberId) {
        String sql = "SELECT * FROM menu WHERE menuname LIKE '%" + name + "%' AND memberId =  '" + memberId + "'";
        return jdbcTemplate.query(sql, new MenuRowMapper());
    }

    //메뉴 이름과 메뉴넘버로 정렬
    @Override
    public List<Menu> findByMenuNameAndMenuNumber(String name, Long menuNumber) {
        String sql = "SELECT * FROM menu WHERE menuname LIKE '%" + name + "%' AND menuNumber =  '" + menuNumber + "'";
        return jdbcTemplate.query(sql, new MenuRowMapper());
    }

    //메뉴 넘버로 레시피 정렬
    @Override
    public List<Recipe> findRecipeByMenuName(Long menuNumber) {
        String sql = "SELECT * FROM recipe WHERE menuNumber = ?";
        return jdbcTemplate.query(sql, new Object[]{menuNumber}, new RecipeRowMapper());
    }

    //메뉴저장
    @Override
    public void saveMenu(Menu menu) {
        String sql = "INSERT INTO menu (menuName, menuWeight, menuCal, menuCar, menuPro, menuFat, menuNa, menuImageUrl, memberId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, menu.getMenuName(), menu.getMenuWeight(), menu.getMenuCal(), menu.getMenuCar(), menu.getMenuPro(), menu.getMenuFat(), menu.getMenuNa(), menu.getMenuImageUrl(), menu.getMemberId());
    }

    //레시피 저장
    @Override
    public void saveRecipe(String recipeDisc, String recipeImgUrl, Long menuNumber) {
        String sql = "INSERT INTO recipe (recipeDisc, recipeImageUrl, menuNumber) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, recipeDisc, recipeImgUrl, menuNumber);
    }

    //메뉴삭제
    @Override
    public void deleteMenu(Long menuNumber) {
        String sql = "DELETE FROM menu WHERE menuNumber = ?";
        jdbcTemplate.update(sql, menuNumber);
    }


    //메뉴 업데이트
    @Override
    public void updateMenu(String menuName, Long menuWeight, Long menuCal, Long menuCar, Long menuPro, Long menuFat, Long menuNa, String menuImage, Long menuNumber) {
        String sql = "UPDATE menu SET menuName = ?, menuWeight = ?, menuCal = ?, menuCar = ? , menuPro = ?, menuFat = ?, menuNa = ?, menuImageUrl = ? WHERE menuNumber = ?";
        jdbcTemplate.update(sql, menuName, menuWeight, menuCal, menuCar, menuPro, menuFat, menuNa, menuImage, menuNumber);
    }


    //레시피 업데이트
    @Override
    public void updateRecipe(String recipes, String recipeImages, Long menuNumber, Long recipeNumber) {
        String sql = "UPDATE recipe SET recipeDisc = ?, recipeImageUrl = ? WHERE menuNumber = ? AND recipeNumber = ?";
        jdbcTemplate.update(sql, recipes, recipeImages, menuNumber, recipeNumber);
    }

    //레시피 삭제
    @Override
    public void deleteRecipe(Long recipeNumber) {
        String sql = "DELETE FROM recipe WHERE recipeNumber = ?";
        jdbcTemplate.update(sql, recipeNumber);
    }

    //특정 메뉴의 모든 댓글 정렬
    @Override
    public List<Comment> findAllByMenu(Long menuNumber) {
        String sql = "SELECT * FROM comment WHERE menuNumber = ?";
        return jdbcTemplate.query(sql, new Object[]{menuNumber}, new CommentRowMapper());
    }

    //댓글저장
    @Override
    public void saveComment(String commentText, Long menuNumber, String memberId) {
        String sql = "INSERT INTO comment (commentText, menuNumber, memberId) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, commentText, menuNumber, memberId);
    }

    //댓글삭제
    @Override
    public void deleteComment(Long commentNumber) {
        String sql = "DELETE FROM comment WHERE commentNumber = ?";
        jdbcTemplate.update(sql, commentNumber);
    }

    //특정 멤버아이디의 댓글정렬
    @Override
    public List<Comment> findByMemberId(String memberId) {
        String sql = "SELECT * FROM comment WHERE memberId = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, new CommentRowMapper());
    }
}
