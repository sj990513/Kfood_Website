package Kim_project.Kfood_Website.controller;

import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.domain.Recipe;
import Kim_project.Kfood_Website.service.MemberService;
import Kim_project.Kfood_Website.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/menu")
@Controller
public class MenuController {

    private final MenuService menuService;
    private  final MemberService memberService;

    @Autowired
    public MenuController(MenuService menuService, MemberService memberService) {
        this.menuService = menuService;
        this.memberService = memberService;
    }

    //메뉴리스트
    @GetMapping("/menuList")
    public String menuList(@RequestParam(name = "sort", required = false) String sortCriteria,
                           @RequestParam(name = "search", required = false) String search,
                           @RequestParam(value = "bookmarkFailed", defaultValue = "", required = false) String bookmarkFailed,
                           Model model) {

        //오류메세지 존재할시 북마크 오류 알림출력
        if(bookmarkFailed.equals("It's already bookmarked"))
            model.addAttribute("bookmarkFailed", bookmarkFailed);

        List<Menu> menus;

        //검색어 존재할시
        if(search != null && !search.isEmpty()) {
            menus = menuService.sortBySearch(search);
            model.addAttribute("menus", menus);
            return "menu/menuList";
        }

        //메뉴 정렬 셀렉트바 값 존재할시
        if(sortCriteria != null && !sortCriteria.isEmpty()) {
            menus = menuService.sortByNutrition(sortCriteria);
            model.addAttribute("menus", menus);
            return "menu/menuList";
        }

        //검색어, 셀렉트바값 없을시 db에 저장된 순서대로 출력
        menus = menuService.sortAllMenu();
        model.addAttribute("menus", menus);
        return "menu/menuList";
    }

    //메뉴 상세보기 (메뉴 정보들, 메뉴에관한 레시피, 메뉴에 달린 댓글들 출력)
    @GetMapping("/detail")
    public String menuDetail(@RequestParam("menuNumber") Long menuNumber,
                             Model model) {

        Optional<Menu> menu = menuService.findMenuNumber(menuNumber);
        List<Recipe> recipes = menuService.sortRecipe(menuNumber);
        List<Comment> comments = menuService.findCommentByMenuNumber(menu.get().getMenuNumber());
        
        //작성자의 이름출력을 위한 코드 (아이디 말고 이름으로 출력)
        //menu테이블에는 작성자의 이름이 들어가있지 않기때문에 이작업을 거쳐야만 한다.
        Optional<Member> member = memberService.findId(menu.get().getMemberId());
        String memberName = member.get().getMemberName();

        model.addAttribute("memberName", memberName);
        model.addAttribute("menu", menu.get());
        model.addAttribute("recipeDetail", recipes);
        model.addAttribute("comments", comments);
        return "menu/menuDetail";
    }

    //메뉴 디테일에서 댓글달기
    @PostMapping("/detail/addComment")
    public String addComment(@RequestParam("commentText") String commentText,
                             @RequestParam("menuNumber") Long menuNumber,
                             HttpSession session) {

        //로그인한사용자
        Optional<Member> loginMember = (Optional<Member>) session.getAttribute("member");

        //댓글추가
        menuService.addComment(commentText, menuNumber, loginMember.get().getMemberId());

        return "redirect:/menu/detail?menuNumber=" + menuNumber;
    }


    //메뉴 디테일에서 댓글삭제
    @PostMapping("/detail/deleteComment")
    public String deleteComment(@RequestParam("commentNumber") Long commentNumber,
                                @RequestParam("menuNumber") Long menuNumber) {

        menuService.deleteComment(commentNumber);

        return "redirect:/menu/detail?menuNumber=" + menuNumber;
    }

    //메뉴추가페이지
    @GetMapping("/addmenu")
    public String addMenu(@RequestParam(value = "addMenuSuccess", defaultValue = "", required = false) String addMenuSuccess,
                          Model model) {

        //메뉴추가 성공메세지 존재시 출력
        if(addMenuSuccess.equals("Menu added successfully"))
            model.addAttribute("addMenuSuccess", addMenuSuccess);

        return "menu/addMenu";
    }

    //메뉴추가
    @PostMapping("/addmenu")
    public String updateMenu(@RequestParam("menu_name") String menuName,
                             @RequestParam("menu_weight") Long menuWeight,
                             @RequestParam("menu_cal") Long menuCal,
                             @RequestParam("menu_car") Long menuCar,
                             @RequestParam("menu_pro") Long menuPro,
                             @RequestParam("menu_fat") Long menuFat,
                             @RequestParam("menu_na") Long menuNa,
                             @RequestParam("menu_image") String menuImage,
                             @RequestParam("recipe[]") String[] recipeDiscs,
                             @RequestParam("recipe_image[]") String[] recipeImages,
                             HttpSession session) throws UnsupportedEncodingException {

        Optional<Member> member = (Optional<Member>) session.getAttribute("member");

        //메뉴세팅
        Menu menu = new Menu();
        menu.setMenuName(menuName);
        menu.setMenuWeight(menuWeight);
        menu.setMenuCal(menuCal);
        menu.setMenuCar(menuCar);
        menu.setMenuPro(menuPro);
        menu.setMenuFat(menuFat);
        menu.setMenuNa(menuNa);
        menu.setMenuImageUrl(menuImage);
        menu.setMemberId(member.get().getMemberId());

        //메뉴추가
        menuService.addMenu(menu);
        //메뉴에대한 레시피추가
        menuService.addRecipe(recipeDiscs, recipeImages);

        //메뉴추가성공메세지 전달
        return "redirect:/menu/addmenu?addMenuSuccess=" + URLEncoder.encode("Menu added successfully", "UTF-8");
    }


    //메뉴삭제시
    @PostMapping("/deleteMenu")
    public String deleteBookmark(@RequestParam("menuNumber") Long menuNumber) {

        menuService.deleteMenu(menuNumber);

        return "redirect:/menu/menuList";
    }

    //메뉴 수정 페이지
    @GetMapping("/editMenu")
    public String editMenu(@RequestParam("menuNumber") Long menuNumber,
                           @RequestParam(name = "editMenuSuccess", required = false) String editMenuSuccess,
                           Model model) {

        Optional<Menu> menu = menuService.findMenuNumber(menuNumber);

        //에딧성공 메시지 존재할시
        if( editMenuSuccess!=null && editMenuSuccess.equals("Menu edited successfully") )
            model.addAttribute("editMenuSuccess", editMenuSuccess);

        //menuNumber도 보내서 POST로도 사용가능하게한다.
        model.addAttribute("menuNumber", menuNumber);
        model.addAttribute("menuName", menu.get().getMenuName());
        model.addAttribute("menuWeight", menu.get().getMenuWeight());
        model.addAttribute("menuCal", menu.get().getMenuCal());
        model.addAttribute("menuCar", menu.get().getMenuCar());
        model.addAttribute("menuPro", menu.get().getMenuPro());
        model.addAttribute("menuFat", menu.get().getMenuFat());
        model.addAttribute("menuNa", menu.get().getMenuNa());
        model.addAttribute("menuImage", menu.get().getMenuImageUrl());

        //해당메뉴에 해당하는 레시피들
        List<Recipe> recipes = menuService.sortRecipe(menu.get().getMenuNumber());
        model.addAttribute("recipes", recipes);

        return "menu/editMenu";
    }


    //메뉴수정
    @PostMapping("/editMenu")
    //menuNumber은 hidden으로 가져온다.
    public String editMenuPage( @RequestParam("menuNumber_hidden") Long menuNumber,
                                @RequestParam("menu_name") String menuName,
                                @RequestParam("menu_weight") Long menuWeight,
                                @RequestParam("menu_cal") Long menuCal,
                                @RequestParam("menu_car") Long menuCar,
                                @RequestParam("menu_pro") Long menuPro,
                                @RequestParam("menu_fat") Long menuFat,
                                @RequestParam("menu_na") Long menuNa,
                                @RequestParam("menu_image") String menuImage,
                                @RequestParam("recipe") String[] recipes,
                                @RequestParam("recipe_image") String[]  recipeImages,
                                RedirectAttributes redirectAttributes) {


        //상세메뉴 업데이트
        menuService.updateMenu(menuName, menuWeight, menuCal, menuCar, menuPro, menuFat, menuNa, menuImage, menuNumber);

        //레시피 업데이트
        menuService.updateRecipe(recipes, recipeImages, menuNumber);

        //업데이트 성공 메세지 GetMapping에 전달
        redirectAttributes.addFlashAttribute("editMenuSuccess", "Menu edited successfully");

        return "redirect:/menu/editMenu?menuNumber=" + menuNumber;
    }
}
