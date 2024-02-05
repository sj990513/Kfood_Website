package Kim_project.Kfood_Website.repository.Bookmark;

import Kim_project.Kfood_Website.domain.Bookmark;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookmarkRepository implements BookmarkRepository{


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBookmarkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    //북마크추가
    public void addBookmark(Member member, Menu menu) {
        String sql = "INSERT INTO bookmark (menuNumber, memberId) VALUES (?, ?)";
        jdbcTemplate.update(sql, menu.getMenuNumber(), member.getMemberId());
    }

    @Override
    //특정멤버 북마크 전체정렬
    public List<Bookmark> FindAllOfMember(Member member) {
        String sql = "SELECT * FROM bookmark WHERE memberId = ?";
        return jdbcTemplate.query(sql, new Object[]{member.getMemberId()}, new BookmarkRowMapper());
    }

    @Override
    //멤버이름으로 북마크찾기
    public Optional<Bookmark> findByMemberId(Member member) {
        String sql = "SELECT * FROM bookmark WHERE memberId = ?";
        List<Bookmark> bookmark = jdbcTemplate.query(sql, new Object[]{member.getMemberId()}, new BookmarkRowMapper());
        return bookmark.stream().findAny();
    }


    @Override
    //메뉴넘버로 북마크찾기
    public Optional<Bookmark> findByMenuNumber(Menu menu) {
        String sql = "SELECT * FROM bookmark WHERE menuNumber = ?";
        List<Bookmark> bookmark = jdbcTemplate.query(sql, new Object[]{menu.getMenuNumber()}, new BookmarkRowMapper());
        return bookmark.stream().findAny();
    }

    @Override
    //특정멤버의 메뉴넘버으로 북마크찾기
    public Optional<Bookmark> findByMemberAndMenu(Member member, Menu menu) {
        String sql = "SELECT * FROM bookmark WHERE memberId = ? and menuNumber = ?";
        List<Bookmark> bookmark = jdbcTemplate.query(sql, new Object[]{member.getMemberId(), menu.getMenuNumber()}, new BookmarkRowMapper());
        return bookmark.stream().findAny();
    }

    @Override
    //특정멤버의 북마크삭제
    public void deleteBookmark(String memberId, Long menuNumber) {
        String sql = "DELETE FROM bookmark WHERE memberId = ? AND menuNumber = ?";
        jdbcTemplate.update(sql, memberId, menuNumber);
    }
}
