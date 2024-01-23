package Kim_project.Kfood_Website.repository.Member;

import Kim_project.Kfood_Website.domain.Member;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getString("memberId"));
        member.setMemberPassword(rs.getString("memberPassword"));
        member.setMemberPhoneNumber(rs.getString("memberPhoneNumber"));
        member.setMemberName(rs.getString("memberName"));
        member.setMemberAuthority(rs.getLong("memberAuthority"));
        member.setMemberEmail(rs.getString("memberEmail"));
        return member;
    }
}
