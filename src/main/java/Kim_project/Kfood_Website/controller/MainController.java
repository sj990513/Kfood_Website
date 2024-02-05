package Kim_project.Kfood_Website.controller;


import Kim_project.Kfood_Website.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    //기본
    @GetMapping("/")
    public String home() {

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return "main";
    }

    //로그인
    @GetMapping("/login")
    public String goLogin(Model model) {
        model.addAttribute("memberDTO", new MemberDTO()); // MemberDTO 객체를 모델에 추가
        return "members/loginPage";
    }

    //로그아웃
    @GetMapping("/logoutCheck")
    public String logoutCheck(HttpServletRequest request,
                              Model model) {

        HttpSession session = request.getSession();
        String logoutSuccessMessage = (String) session.getAttribute("logoutSuccess");

        // 세션삭제
        session.removeAttribute("logoutSuccess");

        model.addAttribute("logoutSuccessMessage", logoutSuccessMessage);

        return "main";
    }

}
