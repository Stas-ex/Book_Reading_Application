package com.diplom.black_fox_ex.controllers.histories;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.service.HistoryService;
import com.diplom.black_fox_ex.request.CreateHistoryDtoReq;

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
            return "history_editing";
        }
        return "redirect:/profile-history";
    }
}
