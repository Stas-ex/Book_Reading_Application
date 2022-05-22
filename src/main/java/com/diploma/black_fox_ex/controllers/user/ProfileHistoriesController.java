package com.diploma.black_fox_ex.controllers.user;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.request.DeleteHistoryDtoReq;
import com.diploma.black_fox_ex.request.UpdateHistoryDtoReq;
import com.diploma.black_fox_ex.service.HistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "user history" page.
 */
@Controller
public class ProfileHistoriesController {
    private final HistoryService historyService;

    @Autowired
    public ProfileHistoriesController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Function to display all stories of a given user
     * @param user retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return all histories to the 'histories' page
     */
    @GetMapping("/profile-history")
    public String viewPage(@AuthenticationPrincipal User user, Model model) {
        var historyDtoResp = historyService.getAllHistoryByUser(user);
        if (historyDtoResp.getError() != null)
            model.addAttribute("errorView", historyDtoResp.getError());
        else
            model.addAttribute("histories", historyDtoResp.getHistoryDto());
        return "/profile/profile-viewHistory";
    }

    /**
     *
     * @param user retrieving Authorized User Data Using Spring Security
     * @param id contains the applied indicator for mutable history
     * @param model for creating attributes sent to the server as a response
     * @return the 'user history' page otherwise error page
     */
    @GetMapping("/profile-history/{id}/delete")
    public String deleteHistory(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new DeleteHistoryDtoReq(id, user);
        var response = historyService.deleteHistory(request);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
            return "errorPage";
        }
        return "redirect:/profile-history";
    }
}
