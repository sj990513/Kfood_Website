package Kim_project.Kfood_Website.repository.Menu;

import Kim_project.Kfood_Website.domain.Menu;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuRowMapper implements RowMapper<Menu> {

    @Override
    public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
        Menu menu = new Menu();
        menu.setMenuNumber(rs.getLong("menuNumber"));
        menu.setMenuName(rs.getString("menuName"));
        menu.setMenuWeight(rs.getLong("menuWeight"));
        menu.setMenuCal(rs.getLong("menuCal"));
        menu.setMenuCar(rs.getLong("menuCar"));
        menu.setMenuPro(rs.getLong("menuPro"));
        menu.setMenuFat(rs.getLong("menuFat"));
        menu.setMenuNa(rs.getLong("menuNa"));
        menu.setMenuImageUrl(rs.getString("menuImageUrl"));
        menu.setMemberId(rs.getString("memberId"));
        return menu;
    }
}
