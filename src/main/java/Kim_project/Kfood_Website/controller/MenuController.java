package Kim_project.Kfood_Website.controller;

import Kim_project.Kfood_Website.convertDTO.ConvertToCommentDTO;
import Kim_project.Kfood_Website.convertDTO.ConvertToMenuDTO;
import Kim_project.Kfood_Website.convertDTO.ConvertToRecipeDTO;
import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.domain.Member;
import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.domain.Recipe;
import Kim_project.Kfood_Website.dto.CommentDTO;
import Kim_project.Kfood_Website.dto.MenuDTO;
import Kim_project.Kfood_Website.dto.RecipeDTO;
import Kim_project.Kfood_Website.service.MemberService;
import Kim_project.Kfood_Website.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        List<MenuDTO> menuDTOs;
        ConvertToMenuDTO convertToMenuDTO = new ConvertToMenuDTO();

        //검색어 존재할시
        if(search != null && !search.isEmpty()) {
            menus = menuService.sortBySearch(search);
            menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus);
            model.addAttribute("menuDTOs", menuDTOs);
            return "menu/menuList";
        }

        //메뉴 정렬 셀렉트바 값 존재할시
        if(sortCriteria != null && !sortCriteria.isEmpty()) {
            menus = menuService.sortByNutrition(sortCriteria);
            menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus);
            model.addAttribute("menuDTOs", menuDTOs);
            return "menu/menuList";
        }

        //검색어, 셀렉트바값 없을시 db에 저장된 순서대로 출력
        menus = menuService.sortAllMenu();


        menuDTOs = convertToMenuDTO.convertToMenuDTOs(menus);

        model.addAttribute("menuDTOs", menuDTOs);
        return "menu/menuList";
    }

    //메뉴 상세보기 (메뉴 정보들, 메뉴에관한 레시피, 메뉴에 달린 댓글들 출력)
    @GetMapping("/detail")
    public String menuDetail(@RequestParam("menuNumber") Long menuNumber,
                             Model model) {

        Optional<Menu> menu = menuService.findMenuNumber(menuNumber);
        List<Recipe> recipes = menuService.sortRecipe(menuNumber);
        List<Comment> comments = menuService.findCommentByMenuNumber(menu.get().getMenuNumber());

        ConvertToRecipeDTO convertToRecipeDTO = new ConvertToRecipeDTO();
        ConvertToCommentDTO convertToCommentDTO = new ConvertToCommentDTO();
        ConvertToMenuDTO convertToMenuDTO = new ConvertToMenuDTO();

        List<RecipeDTO> recipeDTOs = convertToRecipeDTO.convertToRecipeDTO(recipes);
        List<CommentDTO> commentDTOs = convertToCommentDTO.convertToCommentDTOs(comments);
        MenuDTO menuDTO = convertToMenuDTO.convertToMenuDTO(menu.get());

        //작성자의 이름출력을 위한 코드 (아이디 말고 이름으로 출력)
        //menu테이블에는 작성자의 이름이 들어가있지 않기때문에 이작업을 거쳐야만 한다.
        Optional<Member> member = memberService.findId(menu.get().getMemberId());
        String memberName = member.get().getMemberName();

        model.addAttribute("memberName", memberName);
        model.addAttribute("menuDTO", menuDTO);
        model.addAttribute("recipeDTOs", recipeDTOs);
        model.addAttribute("commentDTOs", commentDTOs);
        return "menu/menuDetail";
    }

    //메뉴 디테일에서 댓글달기
    @PostMapping("/detail/addComment")
    public String addComment(@ModelAttribute CommentDTO commentDTO) {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        //댓글추가
        menuService.addComment(commentDTO.getCommentText(), commentDTO.getMenuNumber(), member.get().getMemberId());

        return "redirect:/menu/detail?menuNumber=" + commentDTO.getMenuNumber();
    }


    //메뉴 디테일에서 댓글삭제
    @PostMapping("/detail/deleteComment")
    public String deleteComment(@ModelAttribute CommentDTO commentDTO) {

        menuService.deleteComment(commentDTO.getCommentNumber());

        return "redirect:/menu/detail?menuNumber=" + commentDTO.getMenuNumber();
    }

    //메뉴추가페이지
    @GetMapping("/addmenu")
    public String addMenu(@RequestParam(value = "addMenuSuccess", defaultValue = "", required = false) String addMenuSuccess,
                          Model model) {

        model.addAttribute("menuDTO", new MenuDTO());

        //메뉴추가 성공메세지 존재시 출력
        if(addMenuSuccess.equals("Menu added successfully"))
            model.addAttribute("addMenuSuccess", addMenuSuccess);

        return "menu/addMenu";
    }

    //메뉴추가, 레시피는 스키립트를 사용하여 requestparam으로
    @PostMapping("/addmenu")
    public String updateMenu(@ModelAttribute MenuDTO menuDTO,
                             @RequestParam("recipe[]") String[] recipeDiscs,
                             @RequestParam("recipe_image[]") String[] recipeImages) throws UnsupportedEncodingException {

        //현재 세션유지중인 사용자
        Optional<Member> member = memberService.findId(
                SecurityContextHolder.getContext().getAuthentication()
                        .getName()
        );

        //메뉴세팅
        Menu menu = new Menu();
        menu.setMenuName(menuDTO.getMenuName());
        menu.setMenuWeight(menuDTO.getMenuWeight());
        menu.setMenuCal(menuDTO.getMenuCal());
        menu.setMenuCar(menuDTO.getMenuCar());
        menu.setMenuPro(menuDTO.getMenuPro());
        menu.setMenuFat(menuDTO.getMenuFat());
        menu.setMenuNa(menuDTO.getMenuNa());
        menu.setMenuImageUrl(menuDTO.getMenuImageUrl());
        menu.setMemberId(member.get().getMemberId());

        //메뉴추가
        menuService.addMenu(menu);
        //메뉴에대한 레시피추가
        menuService.addRecipe(recipeDiscs, recipeImages);

        //메뉴추가성공메세지 전달
        return "redirect:/menu/addmenu?addMenuSuccess=" + URLEncoder.encode("Menu added successfully", "UTF-8");
    }


    //메뉴삭제시
    @PostMapping("/detail/deleteMenu")
    public String deleteBookmark(@ModelAttribute MenuDTO menuDTO) {

        menuService.deleteMenu(menuDTO.getMenuNumber());

        return "redirect:/menu/menuList";
    }

    //메뉴 수정 페이지
    @GetMapping("/detail/editMenu")
    public String editMenu(@ModelAttribute MenuDTO getMenuDTO,
                           @RequestParam(name = "editMenuSuccess", required = false) String editMenuSuccess,
                           Model model) {

        Optional<Menu> menu = menuService.findMenuNumber(getMenuDTO.getMenuNumber());
        ConvertToMenuDTO convertToMenuDTO = new ConvertToMenuDTO();
        MenuDTO menuDTO = convertToMenuDTO.convertToMenuDTO(menu.get());

        //에딧성공 메시지 존재할시
        if( editMenuSuccess!=null && editMenuSuccess.equals("Menu edited successfully") )
            model.addAttribute("editMenuSuccess", editMenuSuccess);

        //menuNumber도 보내서 POST로도 사용가능하게한다.
        model.addAttribute("menuDTO", menuDTO);

        //해당메뉴에 해당하는 레시피들
        List<Recipe> recipes = menuService.sortRecipe(menu.get().getMenuNumber());
        model.addAttribute("recipes", recipes);

        return "menu/editMenu";
    }


    //메뉴수정
    @PostMapping("/detail/editMenu")
    //menuNumber은 hidden으로 가져온다.
    public String editMenuPage( @ModelAttribute MenuDTO menuDTO,
                                @RequestParam("recipe") String[] recipes,
                                @RequestParam("recipe_image") String[] recipeImages,
                                RedirectAttributes redirectAttributes) {


        //상세메뉴 업데이트
        menuService.updateMenu(menuDTO.getMenuName(), menuDTO.getMenuWeight(), menuDTO.getMenuCal(), menuDTO.getMenuCar(), menuDTO.getMenuPro(), menuDTO.getMenuFat(),
                menuDTO.getMenuNa(), menuDTO.getMenuImageUrl(), menuDTO.getMenuNumber());

        //레시피 업데이트
        menuService.updateRecipe(recipes, recipeImages, menuDTO.getMenuNumber());

        //업데이트 성공 메세지 GetMapping에 전달
        redirectAttributes.addFlashAttribute("editMenuSuccess", "Menu edited successfully");

        return "redirect:/menu/detail/editMenu?menuNumber=" + menuDTO.getMenuNumber();
    }
}
