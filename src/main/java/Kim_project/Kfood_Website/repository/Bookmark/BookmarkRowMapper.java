package Kim_project.Kfood_Website.repository.Bookmark;

import Kim_project.Kfood_Website.domain.Bookmark;
import Kim_project.Kfood_Website.domain.Menu;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookmarkRowMapper implements RowMapper<Bookmark> {

    @Override
    public Bookmark mapRow(ResultSet rs, int rowNum) throws SQLException {
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberId(rs.getString("memberId"));
        bookmark.setMenuNumber(rs.getLong("menuNumber"));
        return bookmark;
    }
}
