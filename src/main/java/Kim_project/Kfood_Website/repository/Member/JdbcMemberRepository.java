package Kim_project.Kfood_Website.repository.Member;

import Kim_project.Kfood_Website.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO MEMBER (memberId, memberPassword, memberPhoneNumber, memberName, memberAuthority, memberEmail) VALUES (?, ?, ?, ?, ?, ?)";

        //회원가입시 기본권한은 무조건1 (일반회원)
        jdbcTemplate.update(sql, member.getMemberId(), member.getMemberPassword(), member.getMemberPhoneNumber(), member.getMemberName(), 1, member.getMemberEmail());
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        String sql = "SELECT * FROM MEMBER WHERE memberId = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        return members.stream().findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM MEMBER WHERE memberEmail = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberRowMapper());
        return members.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "SELECT * FROM MEMBER WHERE memberName = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{name}, new MemberRowMapper());
        return members.stream().findAny();
    }

    @Override
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM MEMBER WHERE memberPhoneNumber = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{phoneNumber}, new MemberRowMapper());
        return members.stream().findAny();
    }

    @Override
    public void updateUserInfo(String id, String name, String phoneNumber, String password) {
        String sql = "UPDATE MEMBER SET memberName = ?, memberPhoneNumber = ?, memberPassword = ? WHERE memberId = ?";
        jdbcTemplate.update(sql, name, phoneNumber, password, id);
    }

    @Override
    public void deleteAccount(String id) {
        String sql = "DELETE FROM MEMBER WHERE memberId = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM MEMBER";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    @Override
    public void updateAuthority(String memberId, Long memberAuthority) {
        String sql = "UPDATE MEMBER SET memberAuthority = ? WHERE memberId = ?";
        jdbcTemplate.update(sql, memberAuthority, memberId);
    }
}
