package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.request.FavoriteAddRequest;
import com.diplom.black_fox_ex.request.FavoriteDelRequest;
import com.diplom.black_fox_ex.request.FavoriteGetAllUserRequest;
import com.diplom.black_fox_ex.response.FavoriteAddResponse;
import com.diplom.black_fox_ex.response.FavoriteDelResponse;
import com.diplom.black_fox_ex.response.FavoriteGetAllUserResponse;
import com.diplom.black_fox_ex.response.UserDto;
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**-----------------------------------------------------------------------------------**/
@Controller
@RequestMapping("/favorite")
public class FavoriteHistoryController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    private UserService userService;
    @Autowired
    LogInController logIn;
    /**-----------------------------------------------------------------------------------**/
    @GetMapping
    public String startFavoriteHistory(Model model) {
        UserDto userDto = logIn.logIn();
        FavoriteGetAllUserRequest request = new FavoriteGetAllUserRequest(userDto.getId());
        FavoriteGetAllUserResponse response = userService.getAllFavoriteByUser(request);

        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
        }
        model.addAttribute("user", logIn.logInViewUser());
        model.addAttribute("histories", response.getListDto());
        return "favorite-history";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/{id}/add")
    public String addFavoriteHistory(@PathVariable long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = logIn.logIn();
        FavoriteAddRequest request = new FavoriteAddRequest(user, id);
        FavoriteAddResponse response = userService.addFavoriteHistory(request);

        if (response.getError() != null) {
            System.out.println(response.getError());
            model.addAttribute("error", response.getError());
            return "/histories";
        }
        return "redirect:/history/" + id + "/look";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/{id}/delete")
    public String deleteFavoriteHistory(@PathVariable long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userService.getUserByUsername(username);
        FavoriteDelRequest request = new FavoriteDelRequest(id, user);
        FavoriteDelResponse response = userService.deleteFavoriteHistory(request);

        if (response.getError() != null) {
            System.out.println(response.getError());
            model.addAttribute("error", response.getError());
            return "redirect:/favorite";
        }
        return "redirect:/favorite";
    }
}
