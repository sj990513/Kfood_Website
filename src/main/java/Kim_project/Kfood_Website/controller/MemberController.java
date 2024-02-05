package Kim_project.Kfood_Website.controller;

import Kim_project.Kfood_Website.convertDTO.ConvertToCommentDTO;
import Kim_project.Kfood_Website.convertDTO.ConvertToMemberDTO;
import Kim_project.Kfood_Website.convertDTO.ConvertToMenuDTO;
import Kim_project.Kfood_Website.domain.Bookmark;
import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.dto.CommentDTO;
import Kim_project.Kfood_Website.dto.MemberDTO;
import Kim_project.Kfood_Website.dto.MenuDTO;
import Kim_project.Kfood_Website.service.BookmarkService;
import Kim_project.Kfood_Website.service.EmailService;
import Kim_project.Kfood_Website.service.MemberService;
import Kim_project.Kfood_Website.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;
    private final MenuService menuService;
    private final BookmarkService bookmarkService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberController(MemberService memberService, MenuService menuService, BookmarkService bookmarkService, EmailService emailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberService = memberService;
        this.menuService = menuService;
        this.bookmarkService = bookmarkService;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //회원가입페이지
    @GetMapping("/create")
    public String memberPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO()); // MemberDTO 객체를 모델에 추가
        return "members/createMember";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute MemberDTO memberDTO, Model model) {
        // 기존 코드에서 memberDTO로 데이터를 받아오도록 수정

        // 이메일 인증 틀릴때
        if (!memberDTO.getAuthVerify().equals(memberDTO.getAuthString())) {
            model.addAttribute("error", "Wrong Verify");
            return "members/createMember";
        }

        //비밀번호 확인 틀릴때
        if (!memberDTO.getPassword().equals(memberDTO.getPasswordConfirm())) {
            model.addAttribute("error", "Passwords do not match");
            return "members/createMember";
        }

        //BCrypt로 암호화된값 저장
        String rawPassword = memberDTO.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        memberDTO.setPassword(encPassword);

        // MemberDTO를 Member로 변환
        Member member = new Member();
        member.setMemberName(memberDTO.getRealName());
        member.setMemberId(memberDTO.getUserId());
        member.setMemberPhoneNumber(memberDTO.getPhoneNumber());
        member.setMemberPassword(memberDTO.getPassword());
        member.setMemberEmail(memberDTO.getEmailAddress());

        // 이메일 중복 검증
        if (!memberService.validateDuplicateMemberEmail(member)) {
            model.addAttribute("error", "Duplicate Email!");
            return "members/createMember";
        }

        // 아이디 중복 검증
        if (!memberService.validateDuplicateMember(member)) {
            model.addAttribute("error", "Duplicate ID!");
            return "members/createMember";
        }

        // 다 괜찮을 시 회원가입
        memberService.join(member);
        return "members/loginPage";
    }


    //이메일 검증 - 비동기방식 
    @GetMapping("/sendmail")
    @ResponseBody
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email) {
        try {
            // 이메일 보내고 검증문자 받아오기
            String text = emailService.sendEmail(email);
            // 클라이언트에 전달
            return ResponseEntity.ok().body(text);
        } catch (Exception e) {

            // Return an error response to the client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email. Please try again.");
        }
    }

    //아이디찾기페이지
    @GetMapping("/findId")
    public String findIdPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO()); // MemberDTO 객체를 모델에 추가
        return "members/findId";
    }

    //아이디찾기
    @PostMapping("/findId")
    public String findId(@ModelAttribute MemberDTO memberDTO,
                         Model model) {

        //아이디 찾기 성공시
        if(memberService.forgotMemberId(memberDTO.getRealName(), memberDTO.getEmailAddress()) != null) {
            String result = "Your Id is... [" + memberService.forgotMemberId(memberDTO.getRealName(), memberDTO.getEmailAddress()) +"]";
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
    public String findPasswordPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "members/findPassword";
    }

    //패스워드 찾기
    @PostMapping("/findPassword")
    public String findPassword(@ModelAttribute MemberDTO memberDTO,
                               Model model) {

        //해당 이메일 가진 회원 찾기
        if( !memberService.findEmail(memberDTO.getEmailAddress()).isEmpty() ) {

            //존재할시 메일보내기
            Optional<Member> member = memberService.findEmail(memberDTO.getEmailAddress());

            emailService.sendPasswordEmail(memberDTO.getEmailAddress(), member.get());
            model.addAttribute("yourId", "Password has been sent to your email!");
            return "members/loginPage";
        }

        //해당 이메일 가진 회원 없을시
        else {
            model.addAttribute("findPasswordError", "No User corresponding to Email");
            return "members/findPassword";
        }
    }


    //model주입 메소드
    private void populateModelAttributes(Member member, Model model) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserId(member.getMemberId());
        memberDTO.setRealName(member.getMemberName());
        memberDTO.setEmailAddress(member.getMemberEmail());
        memberDTO.setPhoneNumber(member.getMemberPhoneNumber());
        memberDTO.setPassword(member.getMemberPassword());
        memberDTO.setPasswordConfirm(member.getMemberPassword());
        model.addAttribute("memberDTO", memberDTO);
    }

    //마이페이지
    @GetMapping("/mypage")
    public String myPage(@RequestParam(name = "successMessage", required = false) String successMessage,
                         @RequestParam(name = "errorMessage", required = false) String errorMessage,
                         Model model) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        populateModelAttributes(member.get(), model);


        if(successMessage != null)
            model.addAttribute("successMessage", successMessage);

        if(errorMessage != null)
            model.addAttribute("errorMessage", errorMessage);

        return "members/myPage";
    }

    // 마이페이지 수정할시 수정에 사용되는 값을 전송받는다.
    //순수 HTML FORM형태
    @PostMapping("/mypage")
    public String updateMyPageInformation(@ModelAttribute MemberDTO memberDTO,
                                          RedirectAttributes redirectAttributes,
                                          Model model) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        // 비밀번호 확인이 올바른지 확인
        if (!memberDTO.getPassword().equals(memberDTO.getPasswordConfirm())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password is different");
            return "redirect:/members/mypage";
        }

        //비밀번호 암호화
        String rawPassword = memberDTO.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        // 사용자 정보 업데이트
        memberService.update(member.get().getMemberId(), memberDTO.getRealName(), memberDTO.getPhoneNumber(), encPassword);

        redirectAttributes.addFlashAttribute("successMessage", "Change successful!");
        populateModelAttributes(member.get(), model);

        return "redirect:/members/mypage";
    }

    //로그인한 사용자의 작성글
    @GetMapping("/mypage/mypost")
    public String myPost(@RequestParam(name = "search", required = false) String search,
                         Model model) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        List<MenuDTO> menuDTOs;
        ConvertToMenuDTO convertToMenuDTO = new ConvertToMenuDTO();

        //검색어 존재할시
        if(search != null && !search.isEmpty()) {
            //검색어와 메뉴작성한 멤버의 아이디 전달
            List<Menu> menus = menuService.sortBySearchAndMemberId(search, member.get().getMemberId());
            menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus); // 메뉴 dto로 전환
            model.addAttribute("menuDTOs", menuDTOs);
            return "members/myPost";
        }

        //해당 멤버의 메뉴들 정렬
        List<Menu> menus = menuService.sortAllMenuByMemberId(member.get().getMemberId());
        menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus); // 메뉴 dto로 전환
        model.addAttribute("menuDTOs", menuDTOs);

        return "members/myPost";
    }

    //로그인한 사용자의 작성댓글
    @GetMapping("/mypage/myComment")
    public String myComment(Model model) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        List<Comment> comments = menuService.findCommentByMemberId(member.get().getMemberId());
        ConvertToCommentDTO commentDTO = new ConvertToCommentDTO();

        //DTO로 변환
        List<CommentDTO> commentDTOs = commentDTO.convertToCommentDTOs(comments);

        model.addAttribute("commentDTOs", commentDTOs);

        return "members/myComment";
    }

    //댓글삭제
    @PostMapping("/mypage/deleteComment")
    public String deleteComment(@ModelAttribute CommentDTO commentDTO) {

        //댓글 삭제
        menuService.deleteComment(commentDTO.getCommentNumber());

        return "redirect:/members/mypage/myComment";
    }

    /*
    // ajax 실습을 위해 ResponseEntity로 전송
    @PostMapping("/mypage")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleFormData(@RequestBody MemberDTO memberDTO, HttpSession session) {
        // 현재 세션의 사용자 가져오기
        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        // 사용자가 존재하는지 확인
        if (member.isPresent()) {
            // 비밀번호 확인이 올바른지 확인
            if (!memberDTO.getPassword().equals(memberDTO.getPasswordConfirm())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Password is different"));
            }

            // 사용자 정보 업데이트
            memberService.update(member.get().getMemberId(), memberDTO.getRealName(), memberDTO.getPhoneNumber(), memberDTO.getPassword());

            //세션 새로고침
            Optional<Member> updatedMember = memberService.findId(member.get().getMemberId());
            session.setAttribute("member", updatedMember);

            //콘솔확인용
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("ID", member.get().getMemberId());
            responseData.put("Email", member.get().getMemberEmail());
            responseData.put("Name", memberDTO.getRealName());
            responseData.put("PhoneNumber", memberDTO.getPhoneNumber());
            responseData.put("Password", memberDTO.getPassword());

            // responseData 반환
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        // 사용자가 존재하지 않는 경우 오류 응답 반환
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "User not found in session"));
    }

     */

    //삭제페이지
    @GetMapping("/mypage/deletepage")
    public String deletePage() {
        return "members/deletePage";
    }

    //멤버 삭제
    @PostMapping("/mypage/deletepage")
    public String deleteUser() {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        String memberId = member.get().getMemberId();
        memberService.delete(memberId);

        return "redirect:/logout";
    }

    //북마크페이지
    @GetMapping("/bookmark")
    public String bookmarkPage(@RequestParam(name = "search", required = false) String search, Model model) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        //북마크dto가 아닌 메뉴dto로 넘겨주어야 view에서 메뉴들의 정보를 보여줄수 있기때문에 menuDTO리스트를 하나 생성한다.
        List<MenuDTO> menuDTOs = new ArrayList<>();
        ConvertToMenuDTO convertToMenuDTO = new ConvertToMenuDTO();

        // 검색어가 존재할시
        if(search != null && !search.isEmpty()) {

            //검색어 포함된 결과(메뉴)만 넘기기
            List<Menu> menus = bookmarkService.sortByBookmarkAndSearch(search, member.get());
            menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus);
            model.addAttribute("menuDTOs", menuDTOs);
            return "members/bookmarkPage";
        }

        //검색어가 존재하지 않을시 사용자의 모든 북마크 정렬
        List<Bookmark> bookmarks = bookmarkService.sortBookmark(member.get());

        //북마크 정렬된것들을 메뉴리스트에 추가시켜준다.
        for (int i = 0; i < bookmarks.size(); i++) {
            MenuDTO menuDTO = new MenuDTO();
            Menu menu = menuService.findMenuNumber(bookmarks.get(i).getMenuNumber()).get();

            menuDTO.setMenuImageUrl(menu.getMenuImageUrl());
            menuDTO.setMenuName(menu.getMenuName());
            menuDTO.setMenuNumber(menu.getMenuNumber());

            menuDTOs.add(menuDTO);
        }

        //메뉴리스트 넘기기
        model.addAttribute("menuDTOs", menuDTOs);
        return "members/bookmarkPage";
    }


    //북마크추가시 POST로
    //요청 파라미터의 이름과 객체의 필드명이 일치하면 자동으로 매핑됩니다.
    @PostMapping("/addbookmark")
    public String addBookmark(@ModelAttribute MenuDTO menuDTO) throws UnsupportedEncodingException {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        //메뉴
        Optional<Menu> menu = menuService.findMenuNumber(menuDTO.getMenuNumber());

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
    public String deleteBookmark(@ModelAttribute MenuDTO menuDTO) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        bookmarkService.deleteBookmark(member.get(), menuDTO.getMenuNumber());

        return "redirect:/members/bookmark";
    }

    //회원관리페이지
    @GetMapping("/admin")
    public String admin(@RequestParam(value = "updateUserInfoSuccess", defaultValue = "", required = false) String updateUserInfoSuccess,
                        Model model) {

        //업데이트 성공 알림 존재할시
        if( !updateUserInfoSuccess.isEmpty() )
            model.addAttribute("updateUserInfoSuccess", updateUserInfoSuccess);


        //모든멤버들
        List<Member> members = memberService.findMembers();
        ConvertToMemberDTO convertToMemberDTO = new ConvertToMemberDTO();

        List<MemberDTO> memberDTOs = convertToMemberDTO.convertToMemberDTO(members);
        // memberDTOS리스트 전달
        model.addAttribute("memberDTOs", memberDTOs);

        return "members/admin";
    }

    //회원관리 페이지에서 회원 정보 변경시 post로 받아온다.
    @PostMapping("/admin")
    public String updateMemberAuthority(@RequestParam("authentication") List<String> memberAuth,
                                        @RequestParam("userId") List<String> membersId) throws UnsupportedEncodingException {

            //회원 등급변경 (모든회원의 등급을 리스트로 받아와서 업데이트도 모든 회원들을 해준다)
        for(int i = 0; i<membersId.size(); i++)
            memberService.updateMemberAuthority(membersId.get(i), memberAuth.get(i));

        //Get으로 /members/admin에 업데이트 성공 알림 전달
        return "redirect:/members/admin?updateUserInfoSuccess=" + URLEncoder.encode("Update successfully", "UTF-8");
    }

    //회원관리 페이지에서 멤버 삭제 (스크립트로 받아서 requestparam사용)
    @PostMapping("/admin/deleteMember")
    public String memberDelte(@RequestParam("memberId") String memberId) {

        memberService.delete(memberId);

        return "redirect:/members/admin";
    }


    //관리자화면에서 특정 멤버가 작성한 게시글 검색, view에서 특정멤버의 멤버id를 전송받아온다.
    @GetMapping("/admin/searchPost")
    public String memberPostSearch(@RequestParam("memberId") String memberId,
                                   Model model) {

        model.addAttribute("memberId", memberId);

        List<Menu> menus = menuService.sortAllMenuByMemberId(memberId);
        ConvertToMenuDTO convertToMenuDTO = new ConvertToMenuDTO();

        List<MenuDTO> menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus);
        //menuDTO로 전달
        model.addAttribute("menuDTOs", menuDTOs);

        return "members/memberPost";
    }

    //회원관리 페이지(관리자페이지)에서 메뉴(글) 삭제
    @PostMapping("/admin/searchPost/deleteMenu")
    public String deleteMenu(@ModelAttribute MenuDTO menuDTO) {

        //메뉴지우기
        menuService.deleteMenu(menuDTO.getMenuNumber());

        return "redirect:/members/admin/searchPost?memberId=" + menuDTO.getMemberId();
    }


    //관리자화면에서 특정 멤버가 작성한 댓글 검색, view에서 특정멤버의 멤버id를 전송받아온다.
    @GetMapping("/admin/searchComment")
    public String memberCommnetSearch(@RequestParam("memberId") String memberId,
                                   Model model) {

        model.addAttribute("memberId", memberId);

        List<Comment> comments = menuService.findCommentByMemberId(memberId);
        ConvertToCommentDTO convertToCommentDTO = new ConvertToCommentDTO();

        List<CommentDTO> commentDTOS = convertToCommentDTO.convertToCommentDTOs(comments);
        model.addAttribute("commentDTOs", commentDTOS);
        return "members/memberComment";
    }

    
    //관리자화면에서 댓글삭제
    @PostMapping("/admin/searchComment/deleteComment")
    public String deleteCommentForAdmin(@ModelAttribute CommentDTO commentDTO) {

        // 삭제
        menuService.deleteComment(commentDTO.getCommentNumber());

        return "redirect:/members/admin/searchComment?memberId=" + commentDTO.getMemberId();
    }
}

