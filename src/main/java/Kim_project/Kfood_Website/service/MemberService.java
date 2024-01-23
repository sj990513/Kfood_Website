package Kim_project.Kfood_Website.service;

import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.repository.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원가입
    public String join(Member member) {

        memberRepository.save(member);
        return member.getMemberId();
    }
    
    //iD로 중복회원검증
    public boolean validateDuplicateMember(Member member) {
        Optional<Member> existingMember = memberRepository.findById(member.getMemberId());

        return existingMember.isEmpty();
    }

    //이메일로 중복회원검증
    public boolean validateDuplicateMemberEmail(Member member) {
        Optional<Member> existingMember = memberRepository.findByEmail(member.getMemberEmail());

        return existingMember.isEmpty();
    }

    //이메일로 멤버 찾기
    public Optional<Member> findEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    //모든 멤버 리스트 반환
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //id로 멤버 찾기
    public Optional<Member> findId(String id) {
        return memberRepository.findById(id);
    }

    //이름으로 멤버찾기
    public Optional<Member> findName(String name) {
        return memberRepository.findByName(name);
    }

    //폰번호로 멤버찾기
    public Optional<Member> findPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }

    //이름, 이메일로 멤버 아이디 찾기
    public String forgotMemberId(String name, String email) {
        Optional<Member> memberName = memberRepository.findByName(name);
        Optional<Member> memberEmail = memberRepository.findByEmail(email);

        //둘다 존재하면
        if(!memberName.isEmpty() && !memberEmail.isEmpty()) {

            //두개에서 꺼낸 멤버의 id가 일치한지 확인
            boolean findName = memberName.get().getMemberId().equals( memberEmail.get().getMemberId() );

            //일치하면 아이디 반환
            if (findName)
                return memberName.get().getMemberId();

            //아닐시 null반환
            else
                return null;
        }
        return null;
    }

    //로그인
    public boolean login(String id, String pw) {
        //해당 아이디를 가진 멤버 존재시
        Optional<Member> member = memberRepository.findById(id);

        //db에 저장된 비밀번호와 사용자가 입력한 pw가 일치할시 로그인성공
        if (member.isPresent() && member.get().getMemberPassword().equals(pw))
            return true;

        else
            return false;
    }

    //회원정보업데이트
    public void update(String id, String userRealName, String userPhoneNumber, String password) {
        memberRepository.updateUserInfo(id, userRealName, userPhoneNumber, password);
    }

    //회원삭제
    public void delete(String id) {
        memberRepository.deleteAccount(id);
    }

    //회원권한수정
    public void updateMemberAuthority(String memberId, String memberAuthority) {

        Long memberAuth = Long.parseLong(memberAuthority);

        memberRepository.updateAuthority(memberId, memberAuth);
    }

}
