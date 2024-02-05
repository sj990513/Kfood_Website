package Kim_project.Kfood_Website.config;

import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.service.MemberService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MyMemberService implements UserDetailsService {

    private final MemberService memberService;

    public MyMemberService(MemberService memberService) {
        this.memberService = memberService;
    }


    //스프링 부트 3.0.6은 thymeleaf-extras-springsecurity6버전과 호환된다.
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> findOne  = memberService.findId(userId);
        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("This member does not exist."));


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getMemberAuthority())); // "ROLE_" 접두어는 역할에 대한 흔한 관례


        return new User(member.getMemberId(), member.getMemberPassword(), authorities);
    }
}
