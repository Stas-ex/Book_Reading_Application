package com.diplom.black_fox_ex.controllers.user;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.request.DeleteHistoryDtoReq;
import com.diplom.black_fox_ex.request.UpdateHistoryDtoReq;
import com.diplom.black_fox_ex.service.HistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "user history" page.
 */
@Controller
@RequestMapping("/profile-history")
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
    @GetMapping
    public String viewPage(@AuthenticationPrincipal User user, Model model) {
        var historyDtoResp = historyService.getAllHistoryByUser(user);
        if (historyDtoResp.getError() != null)
            model.addAttribute("errorView", historyDtoResp.getError());
        else
            model.addAttribute("histories", historyDtoResp.getHistoryDto());
        return "/profile/profile-viewHistory";
    }

    /**
     * The function needed to go to the page of all stories
     * @param user retrieving Authorized User Data Using Spring Security
     * @param id contains the unique indicator for mutable history
     * @param model for creating attributes sent to the server as a response
     * @return page title, list tags, history to the 'history editing' page
     */
    @GetMapping("/{id}/editPage")
    public String editingHistoryPage(@AuthenticationPrincipal User user,
                                     @PathVariable long id, Model model) {
        var response = historyService.getHistory(user, id);
        if (response.getError() != null) {
            return "redirect:/profile-history";
        }

        model.addAttribute("title", "Update");
        model.addAttribute("tags", historyService.getAllTag());
        model.addAttribute("historyDto", response.getHistoryDto());
        return "/history/history_editing";
    }

    /**
     * The function needed to update the generated history
     * @param user retrieving Authorized User Data Using Spring Security
     * @param historyDto containing a request from the user
     * @param model for creating attributes sent to the server as a response
     * @return 'user histories' page
     */
    @PostMapping("/{id}/update")
    public String updateHistory(@AuthenticationPrincipal User user,
                                @ModelAttribute UpdateHistoryDtoReq historyDto, Model model) {
        var response = historyService.updateHistory(user, historyDto);
        if (response.getError() != null) {
            model.addAttribute("historyDto", historyDto);
            model.addAttribute("title", "Update");
            model.addAttribute("tags", historyService.getAllTag());
            model.addAttribute("id", historyDto.getId());
            model.addAttribute("errorCreateHistory", response.getError());
            return "history_editing";
        }
        return "redirect:/profile-history";
    }

    /**
     *
     * @param user retrieving Authorized User Data Using Spring Security
     * @param id contains the applied indicator for mutable history
     * @param model for creating attributes sent to the server as a response
     * @return the 'user history' page otherwise error page
     */
    @GetMapping("/{id}/delete")
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
