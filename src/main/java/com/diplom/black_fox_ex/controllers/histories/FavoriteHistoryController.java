package com.diplom.black_fox_ex.controllers.histories;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.request.AddFavoriteHiReq;
import com.diplom.black_fox_ex.request.DeleteFavoriteHiDtoReq;
import com.diplom.black_fox_ex.service.HistoryService;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

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

    @GetMapping
    public String startFavoriteHistory(@AuthenticationPrincipal User user, Model model) {
        var responseDto = userService.getAllFavoriteByUser(user);
        if (responseDto.getError() != null) {
            model.addAttribute("errorFavorite", responseDto.getError());
        }
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("histories", responseDto.getListDto());
        return "favorite-history";
    }

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
