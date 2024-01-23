package Kim_project.Kfood_Website.controller;

import Kim_project.Kfood_Website.domain.Bookmark;
import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.service.BookmarkService;
import Kim_project.Kfood_Website.service.EmailService;
import Kim_project.Kfood_Website.service.MemberService;
import Kim_project.Kfood_Website.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MenuService menuService;
    private final BookmarkService bookmarkService;
    private final EmailService emailService;

    @Autowired
    public MemberController(MemberService memberService, MenuService menuService, BookmarkService bookmarkService, EmailService emailService) {
        this.memberService = memberService;
        this.menuService = menuService;
        this.bookmarkService = bookmarkService;
        this.emailService = emailService;
    }

    //회원가입페이지
    @GetMapping("/create")
    public String memberPage() {
        return "members/createMember";
    }

    @PostMapping("/create")
    public String create(@RequestParam("userId") String userId,
                         @RequestParam("userName") String realName,
                         @RequestParam("userPhoneNumber") String phoneNumber,
                         @RequestParam("userPassword") String password,
                         @RequestParam("password_confirm") String passwordConfirm,
                         @RequestParam("userEmail") String emailAddress,
                         //사용자가쓴거
                         @RequestParam("authString") String authString,
                         //실제인증번호
                         @RequestParam("authStringHidden") String authVerify,
                         Model model) {

        //비밀번호 확인 틀릴때
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Password is different");
            return "members/createMember";
        }

        //계정 인증 틀릴때
        if(!authVerify.equals(authString)) {
            model.addAttribute("error", "Wrong Verify");
            return "members/createMember";
        }

        //멤버세팅
        Member member = new Member();
        member.setMemberName(realName);
        member.setMemberId(userId);
        member.setMemberPhoneNumber(phoneNumber);
        member.setMemberPassword(password);
        member.setMemberEmail(emailAddress);

        //이메일 중복 검증
        if( !memberService.validateDuplicateMemberEmail(member) ) {
            model.addAttribute("error", "Duplicate Email!");
            return "members/createMember";
        }
        
        // 아이디 중복 검증
        if ( !memberService.validateDuplicateMember(member) ) {
            model.addAttribute("error", "Duplicate ID!");
            return "members/createMember";
        }

        // 다괜찮을시 회원가입
        memberService.join(member);
        return "members/loginPage";
    }


    //이메일 검증 - 비동기방식 
    @GetMapping("/sendmail")
    @ResponseBody
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {

        // 이메일 보내고 검증문자 받아오기
        String text = emailService.sendEmail(email);

        // 클라이언트에 전달
        return ResponseEntity.ok().body(text);
    }

    //로그인체크
    @PostMapping("/loginCheck")
    public String loginCheck(@RequestParam("id") String memberId,
                             @RequestParam("pw") String memberPassword,
                             HttpSession session,
                             Model model) {

        //로그인결과
        Boolean result = memberService.login(memberId, memberPassword);

        //로그인성공
        if (result) {
            Optional<Member> member = memberService.findId(memberId);

            //세션만들기
            session.setAttribute("member", member);
            return "redirect:/";
        }

        //로그인실패
        else {
            model.addAttribute("loginError", "ID or password error");
            return "members/loginPage";
        }
    }

    //아이디찾기페이지
    @GetMapping("/findId")
    public String findIdPage() {
        return "members/findId";
    }

    //아이디찾기
    @PostMapping("/findId")
    public String findId(@RequestParam("real_name") String username,
                         @RequestParam("user_email") String user_email,
                         Model model) {

        //아이디 찾기 성공시
        if(memberService.forgotMemberId(username, user_email) != null) {
            String result = "Your Id is... [" + memberService.forgotMemberId(username, user_email) +"]";
            model.addAttribute("yourId", result);
            return "members/loginPage";
        }

        //아이디 찾기 실패시
        else {
            model.addAttribute("findIdError", "No ID corresponding to Name and Email");
            return "members/findId";
        }
    }

    //패스워드 찾기 페이지
    @GetMapping("/findPassword")
    public String findPasswordPage() {
        return "members/findPassword";
    }

    //패스워드 찾기
    @PostMapping("/findPassword")
    public String findPassword(@RequestParam("user_email") String user_email,
                               Model model) {

        //해당 이메일 가진 회원 찾기
        if(memberService.findEmail(user_email) != null) {

            //존재할시 메일보내기
            Optional<Member> member = memberService.findEmail(user_email);

            emailService.sendPasswordEmail(user_email, member.get());
            model.addAttribute("yourId", "Password has been sent to your email!");
            return "members/loginPage";
        }

        //해당 이메일 가진 회원 없을시
        else {
            model.addAttribute("findIdError", "No User corresponding to Email");
            return "members/findId";
        }
    }


    //마이페이지에서 사용하기 위한 model주입 메소드
    private void populateModelAttributes(Member member, Model model) {
        model.addAttribute("userId", member.getMemberId());
        model.addAttribute("userEmail", member.getMemberEmail());
        model.addAttribute("userRealName", member.getMemberName());
        model.addAttribute("userPhoneNumber", member.getMemberPhoneNumber());
        model.addAttribute("userPassword", member.getMemberPassword());
    }

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session,
                         Model model) {

        //현재 세션유지중인 사용자
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");
        //사용자가 존재할시
        if (member != null) {
            populateModelAttributes(member.get(), model);
            return "members/myPage";
        }

        else
            return "main";
    }


    // 마이페이지 수정할시 수정에 사용되는 이름, 핸드폰번호, 비밀번호, 비밀번호 확인만 값을 전송받는다.
    // ajax 실습을 위해 ResponseEntity로 전송
    @PostMapping("/mypage")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleFormData(@RequestBody Map<String, Object> formData,
                                                              HttpSession session) {

        // 현재 세션의 사용자 가져오기
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        // 사용자가 존재하는지 확인
        if (member.isPresent()) {

            // 비밀번호 확인이 올바른지 확인
            if (!formData.get("password").equals(formData.get("passwordConfirm"))) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Password is different"));
            }

            // 사용자 정보 업데이트
            memberService.update(member.get().getMemberId(), (String) formData.get("userRealName"),
                    (String) formData.get("userPhoneNumber"), (String) formData.get("password"));


            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userId", member.get().getMemberId());
            responseData.put("userEmail", member.get().getMemberEmail());
            responseData.put("userRealName", member.get().getMemberName());
            responseData.put("userPhoneNumber", member.get().getMemberPhoneNumber());
            responseData.put("userPassword", member.get().getMemberPassword());

            // responseData 반환
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        // 사용자가 존재하지 않는 경우 오류 응답 반환
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "User not found in session"));
    }



    /* 순수 HTML FORM형태
    @PostMapping("/members/mypage123")
    public String updateMyPageInformation(@RequestParam("userRealName") String userRealName,
                                          @RequestParam("userPhoneNumber") String userPhoneNumber,
                                          @RequestParam("password") String password,
                                          @RequestParam("passwordConfirm") String passwordConfirm,
                                          HttpSession session,
                                          Model model) {

        //현재 세션유지중인 사용자
         Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        //멤버가 존재할시
        if (member.isPresent()) {

            //비밀번호 확인이 틀렸을시
            if ( !password.equals(passwordConfirm) ) {
                model.addAttribute("error", "Password is different");
                populateModelAttributes(member.get(), model);
                return "members/myPage";
            }
            
            //이외 정상일시 멤버 업데이트
            memberService.update(member.get().getMemberId(), userRealName, userPhoneNumber, password);
            member.get().setMemberName(userRealName);
            member.get().setMemberPhoneNumber(userPhoneNumber);
            member.get().setMemberPassword(password);

            model.addAttribute("success", "Change successful!");
            populateModelAttributes(member.get(), model);

            return "members/myPage";
        }

        //세션 유지중인 사용자없을시 로그인페이지로
        return "members/login";
    }
     */

    //삭제페이지
    @GetMapping("/deletepage")
    public String deletePage() {
        return "members/deletePage";
    }

    //멤버 삭제
    @PostMapping("/deletepage")
    public String deleteUser(HttpSession session) {

        //현재 세션 사용자
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");
        String memberId = member.get().getMemberId();
        memberService.delete(memberId);

        return "redirect:/logout";
    }

    //북마크페이지
    @GetMapping("/bookmark")
    public String bookmarkPage(@RequestParam(name = "search", required = false) String search,
                               HttpSession session, Model model) {

        //로그인한 사용자
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        // 검색어가 존재할시
        if(search != null && !search.isEmpty()) {

            //검색어 포함된 결과(메뉴)만 넘기기
            List<Menu> menus = bookmarkService.sortByBookmarkAnd(search, member.get());
            model.addAttribute("menus", menus);
            return "members/bookmarkPage";
        }

        //검색어가 존재하지 않을시 사용자의 모든 북마크 정렬
        List<Bookmark> bookmarks = bookmarkService.sortBookmark(member.get());

        //북마크도메인이 아닌 메뉴 도메인으로 넘겨주어야 view에서 메뉴들의 정보를 보여줄수 있기때문에 menu리스트를 하나 생성한다.
        List<Menu> menus = new ArrayList<>();

        //북마크 정렬된것들을 메뉴리스트에 추가시켜준다.
        for(int i=0; i<bookmarks.size(); i++)
            menus.add( menuService.findMenuNumber( bookmarks.get(i).getMenuNumber() ).get() );

        //메뉴리스트 넘기기
        model.addAttribute("menus", menus);
        return "members/bookmarkPage";
    }

    //북마크추가시 POST로
    @PostMapping("/addbookmark")
    public String addBookmark(@RequestParam("menuNumber") Long menuNumber,
                               HttpSession session, Model model) throws UnsupportedEncodingException {

        //로그인한 사용자
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        //메뉴
        Optional<Menu> menu = menuService.findMenuNumber(menuNumber);

        //중복확인
        if(bookmarkService.validateMenu(member.get(), menu.get())) {
            //중복아니면 메뉴추가
            bookmarkService.addBookmark(member.get(), menu.get());
            return "redirect:/members/bookmark";
        }

        //중복이면 알람출력
        else {
            return "redirect:/menu/menuList?bookmarkFailed=" + URLEncoder.encode("It's already bookmarked", "UTF-8");
        }
    }

    //북마크삭제시
    @PostMapping("/deleteBookmark")
    public String deleteBookmark(@RequestParam("menuNumber") Long menuNumber,
                                 HttpSession session, Model model) {

        //로그인한 사용자
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        bookmarkService.deleteBookmark(member.get(), menuNumber);

        return "redirect:/members/bookmark";
    }

    //회원관리페이지
    @GetMapping("/admin")
    public String admin(@RequestParam(value = "updateUserInfoSuccess", defaultValue = "", required = false) String updateUserInfoSuccess,
                        Model model) {
        //모든멤버들
        List<Member> members = memberService.findMembers();

        //업데이트 성공 알림 존재할시
        if( !updateUserInfoSuccess.isEmpty() )
            model.addAttribute("updateUserInfoSuccess", updateUserInfoSuccess);
        model.addAttribute("members", members);

        return "members/admin";
    }

    //회원관리 페이지에서 회원 정보 변경시 post로 받아온다.
    @PostMapping("/admin")
    public String updateMemberAuthority(@RequestParam("memberAuth") List<String> memberAuth,
                                        @RequestParam("memberId") List<String> membersId,
                                        Model model) throws UnsupportedEncodingException {

        //회원 등급변경 (모든회원의 등급을 리스트로 받아와서 업데이트도 모든 회원들을 해준다)
        for(int i = 0; i<membersId.size(); i++)
            memberService.updateMemberAuthority(membersId.get(i), memberAuth.get(i));

        //Get으로 /members/admin에 업데이트 성공 알림 전달
        return "redirect:/members/admin?updateUserInfoSuccess=" + URLEncoder.encode("Update successfully", "UTF-8");
    }

    //회원관리 페이지에서 멤버 삭제
    @PostMapping("/deleteMember")
    public String memberDelte(@RequestParam("memberId") String memberId) {

        memberService.delete(memberId);

        return "redirect:/members/admin";
    }

    
    
    
    //로그인한 사용자의 작성글
    @GetMapping("/myPost")
    public String myPost(@RequestParam(name = "search", required = false) String search,
                         HttpSession session,
                         Model model) {

        //로그인 중인 사용자
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        List<Menu> menus;

        //검색어 존재할시 
        if(search != null && !search.isEmpty()) {
            //검색어와 메뉴작성한 멤버의 아이디 전달
            menus = menuService.sortBySearchAndMemberId(search, member.get().getMemberId());
            model.addAttribute("menus", menus);
            return "members/myPost";
        }

        //해당 멤버의 메뉴들 정렬
        menus = menuService.sortAllMenuByMemberId(member.get().getMemberId());
        model.addAttribute("menus", menus);

        return "members/myPost";
    }


    //로그인한 사용자의 작성댓글
    @GetMapping("/myComment")
    public String myComment(HttpSession session,
                            Model model) {

        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        List<Comment> comments = menuService.findCommentByMemberId(member.get().getMemberId());

        model.addAttribute("comments", comments);

        return "members/myComment";
    }

    //댓글삭제
    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentNumber") Long commentNumber,
                                HttpSession session,
                                Model model) {

        //댓글 삭제
        menuService.deleteComment(commentNumber);

        return "redirect:/members/myComment";
    }

    //관리자화면에서 특정 멤버가 작성한 게시글 검색, view에서 특정멤버의 멤버id를 전송받아온다.
    @GetMapping("/searchPost")
    public String memberPostSearch(@RequestParam("memberId") String memberId,
                                   Model model) {

        model.addAttribute("memberId", memberId);

        List<Menu> menus;

        menus = menuService.sortAllMenuByMemberId(memberId);
        model.addAttribute("menus", menus);

        return "members/memberPost";
    }


    //관리자화면에서 특정 멤버가 작성한 댓글 검색, view에서 특정멤버의 멤버id를 전송받아온다.
    @GetMapping("/searchComment")
    public String memberCommnetSearch(@RequestParam("memberId") String memberId,
                                   Model model) {

        model.addAttribute("memberId", memberId);

        List<Comment> comments = menuService.findCommentByMemberId(memberId);

        model.addAttribute("comments", comments);

        return "members/memberComment";
    }

    
    //관리자화면에서 댓글삭제
    @PostMapping("/searchComment")
    public String deleteCommentForAdmin(@RequestParam("commentNumber") Long commentNumber,
                                        @RequestParam("memberId") String memberId) {

        // 삭제
        menuService.deleteComment(commentNumber);

        return "redirect:/members/searchComment?memberId=" + memberId;
    }


    //회원관리 페이지(관리자페이지)에서 메뉴(글) 삭제
    @PostMapping("/deleteMenu")
    public String deleteMenu(@RequestParam("memberId") String memberId,
                             @RequestParam("menuNumber") Long menuNumber) {

        //메뉴지우기
        menuService.deleteMenu(menuNumber);

        return "redirect:/members/searchPost?memberId=" + memberId;
    }
}

