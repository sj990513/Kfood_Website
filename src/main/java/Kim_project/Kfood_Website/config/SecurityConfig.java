package Kim_project.Kfood_Website.config;


import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/assets/**","/", "/logoutCheck","/menu/menuList", "/menu/detail", "/members/loginCheck",
                                "/members/sendmail", "/members/create", "/members/findId", "/members/findPassword").permitAll()
                        .requestMatchers("/members/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()	// 이외 다른 어떠한 요청이라도 인증필요
                )
                .formLogin(login -> login	// form 방식 로그인 사용
                        .loginPage("/login")	// [A] 커스텀 로그인 페이지 지정
                        .failureUrl("/login")
                        .loginProcessingUrl("/members/loginCheck")	// [B] submit 받을 url
                        .usernameParameter("userId")	// [C] submit할 아이디
                        .passwordParameter("password")	// [D] submit할 비밀번호
                        .defaultSuccessUrl("/", true)
                        .successHandler(
                                new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        response.sendRedirect("/"); // 인증이 성공한 후에는 root로 이동
                                    }
                                }
                        )
                        .failureHandler(
                                new AuthenticationFailureHandler() {
                                    @Override
                                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                        request.getSession().setAttribute("loginError", "ID or password error. Please try again."); // Set error message in session
                                        response.sendRedirect("/login");
                                    }
                                }
                        )
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키 설정, 여러 개일 경우 여러 번 호출
                        .logoutSuccessHandler(
                                new LogoutSuccessHandler() {
                                    @Override
                                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        request.getSession().setAttribute("logoutSuccess", "Logout successful");
                                        response.sendRedirect("/logoutCheck");
                                    }
                                }
                        )
                );

        return http.build();
    }   

    //BCrypt암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoderpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}