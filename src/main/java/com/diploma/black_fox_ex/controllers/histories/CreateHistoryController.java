package com.diploma.black_fox_ex.controllers.histories;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.request.UpdateHistoryDtoReq;
import com.diploma.black_fox_ex.service.HistoryService;
import com.diploma.black_fox_ex.request.CreateHistoryDtoReq;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "create history" page.
 */
@Controller
@RequestMapping("/history")
public class CreateHistoryController {
    private final HistoryService historyService;

    @Autowired
    public CreateHistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * The function to go to the story creation page
     * @param historyDto designed to send a non-null element to the server
     * @param model for creating attributes sent to the server as a response
     * @return  list tags, title, to the 'history editing' page
     */
    @GetMapping("/createPage")
    public String profileCreateHistory(@ModelAttribute CreateHistoryDtoReq historyDto, Model model) {
        model.addAttribute("tags", historyService.getAllTag());
        model.addAttribute("title", "Create");
        model.addAttribute("historyDto", historyDto);
        return "/history/history_editing";
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
        var response = historyService.getHistoryEditById(user, id);
        if (response.getError() != null) {
            return "redirect:/profile-history";
        }

        model.addAttribute("title", "Update");
        model.addAttribute("tags", historyService.getAllTag());
        model.addAttribute("historyDto", response.getHistoryDto());
        return "/history/history_editing";
    }

    /**
     * Function to create history
     * @param user retrieving Authorized User Data Using Spring Security
     * @param historyDto containing a request from the user
     * @param model for creating attributes sent to the server as a response
     * @return the 'user histories' page
     */
    @PostMapping("/create")
    public String createHistory(@AuthenticationPrincipal User user,
                                @ModelAttribute CreateHistoryDtoReq historyDto,
                                Model model) {
        var response = historyService.createHistory(user, historyDto);
        if (response.getError() != null) {
            model.addAttribute("title", "Create");
            model.addAttribute("historyDto", historyDto);
            model.addAttribute("tags", historyService.getAllTag());
            model.addAttribute("errorCreateHistory", response.getError());
            return "/history/history_editing";
        }
        return "redirect:/profile-history";
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
            return "history/history_editing";
        }
        return "redirect:/profile-history";
    }

}
