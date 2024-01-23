package Kim_project.Kfood_Website.repository.Menu;

import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Menu;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentNumber(rs.getLong("commentNumber"));
        comment.setCommentText(rs.getString("commentText"));
        comment.setMemberId(rs.getString("memberId"));
        comment.setMenuNumber(rs.getLong("menuNumber"));
        return comment;
    }
}
