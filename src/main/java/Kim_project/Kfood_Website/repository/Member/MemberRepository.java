package Kim_project.Kfood_Website.repository.Member;

import Kim_project.Kfood_Website.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(String id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    void updateUserInfo(String id, String name, String phoneNumber, String password);
    void deleteAccount(String id);
    List<Member> findAll();
    void updateAuthority(String memberId, String memberAuthority);
}
