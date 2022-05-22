package com.diploma.black_fox_ex.controllers.histories;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.request.AddFavoriteHiReq;
import com.diploma.black_fox_ex.request.DeleteFavoriteHiDtoReq;
import com.diploma.black_fox_ex.service.HistoryService;
import com.diploma.black_fox_ex.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "favorite history" page.
 */
@Controller
@RequestMapping("/favorite")
public class FavoriteHistoryController {
    private final UserService userService;
    private final HistoryService historyService;

    @Autowired
    public FavoriteHistoryController(UserService userService, HistoryService historyService) {
        this.userService = userService;
        this.historyService = historyService;
    }

    /**
     * Function to go to the 'favorite history' page
     * @param user retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return user histories to the 'favorite history' page
     */
    @GetMapping
    public String startFavoriteHistory(@AuthenticationPrincipal User user, Model model) {
        var responseDto = historyService.getAllFavoriteByUser(user);
        if (responseDto.getError() != null) {
            model.addAttribute("errorFavorite", responseDto.getError());
        }
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("histories", responseDto.getListDto());
        return "/history/favorite-history";
    }

    /**
     * Function to add history to the saved section
     * @param user retrieving Authorized User Data Using Spring Security
     * @param id contains the applied indicator for mutable history
     * @param model for creating attributes sent to the server as a response
     * @return the 'history look' page otherwise error page
     */
    @GetMapping("/{id}/add")
    public String addFavoriteHistory(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new AddFavoriteHiReq(user, id);
        var response = historyService.addFavoriteHistory(request);
        if (response.getError() != null) {
            System.out.println(response.getError());
            model.addAttribute("error", response.getError());
            return "/errorPage";
        }
        return "redirect:/history/%d/look".formatted(id);
    }

    /**
     * Function to delete history from section "Favorites"
     * @param user retrieving Authorized User Data Using Spring Security
     * @param id contains the applied indicator for mutable history
     * @param model for creating attributes sent to the server as a response
     * @return the 'favorite histories' page otherwise error page
     */
    @GetMapping("/{id}/delete")
    public String deleteFavoriteHistory(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new DeleteFavoriteHiDtoReq(id, user);
        var response = historyService.deleteFavoriteHistory(request);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
            return "errorPage";
        }
        return "redirect:/favorite";
    }
}
