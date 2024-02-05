package Kim_project.Kfood_Website.convertDTO;

import Kim_project.Kfood_Website.domain.Menu;
import Kim_project.Kfood_Website.dto.MenuDTO;

import java.util.ArrayList;
import java.util.List;

public class ConvertToMenuDTO {
    public List<MenuDTO> convertToMenuDTOs(List<Menu> menus) {
        List<MenuDTO> menuDTOs = new ArrayList<>();
        for (Menu menu : menus) {
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setMenuNumber(menu.getMenuNumber());
            menuDTO.setMenuName(menu.getMenuName());
            menuDTO.setMenuWeight(menu.getMenuWeight());
            menuDTO.setMenuCal(menu.getMenuCal());
            menuDTO.setMenuCar(menu.getMenuCar());
            menuDTO.setMenuPro(menu.getMenuPro());
            menuDTO.setMenuFat(menu.getMenuFat());
            menuDTO.setMenuNa(menu.getMenuNa());
            menuDTO.setMenuImageUrl(menu.getMenuImageUrl());
            menuDTO.setMemberId(menu.getMemberId());
            menuDTOs.add(menuDTO);
        }
        return menuDTOs;
    }

    public MenuDTO convertToMenuDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();

        if (menu != null) {
            menuDTO.setMenuNumber(menu.getMenuNumber());
            menuDTO.setMenuName(menu.getMenuName());
            menuDTO.setMenuWeight(menu.getMenuWeight());
            menuDTO.setMenuCal(menu.getMenuCal());
            menuDTO.setMenuCar(menu.getMenuCar());
            menuDTO.setMenuPro(menu.getMenuPro());
            menuDTO.setMenuFat(menu.getMenuFat());
            menuDTO.setMenuNa(menu.getMenuNa());
            menuDTO.setMenuImageUrl(menu.getMenuImageUrl());
            menuDTO.setMemberId(menu.getMemberId());
        }

        return menuDTO;
    }
}
