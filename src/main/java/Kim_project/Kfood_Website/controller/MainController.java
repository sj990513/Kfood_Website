package Kim_project.Kfood_Website.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    //기본
    @GetMapping("/")
    public String home() {
        return "main";
    }

    //로그인
    @GetMapping("/login")
    public String goLogin() {

        return "members/loginPage";
    }

    //로그아웃
    @GetMapping("/logout")
    public String logoutGET(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        
        //로그아웃시 세션종료
        session.invalidate();

        model.addAttribute("logout", "logout");
        return "main";
    }
}
