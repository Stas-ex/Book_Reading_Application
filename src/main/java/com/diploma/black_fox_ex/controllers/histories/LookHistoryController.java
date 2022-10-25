package com.diploma.black_fox_ex.controllers.histories;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.dto.AddCommentDtoReq;
import com.diploma.black_fox_ex.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

/**
 * This is the class for interacting with the 'comment creator' page.
 */
@Controller
@RequestMapping("/history/{id}")
public class LookHistoryController {
    private final HistoryService historyService;

    @Autowired
    public LookHistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Go to history page function
     * @param user retrieving Authorized User Data Using Spring Security
     * @param id id contains a unique history identifier
     * @param model for creating attributes sent to the server as a response
     * @return like, history to the 'history look' page
     */
    @GetMapping("/look")
    public String lookHistory(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var response = historyService.getHistoryById(user, id);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
            System.out.println("error");
            return "redirect:/histories";
        }
        model.addAttribute("likeActive", response.getLikeActive());
        model.addAttribute("historyLook", response.getHistoryDto());
        return "/history/history-look";
    }

    /**
     *
     * @param commentDto creating a null model to render the page
     * @param id id contains a unique history identifier
     * @param model for creating attributes sent to the server as a response
     * @return id to the 'comment creator' page
     */
    @GetMapping("/comment")
    public String comment(@ModelAttribute AddCommentDtoReq commentDto, @PathVariable long id, Model model) {
        model.addAttribute("commentDto", commentDto);
        model.addAttribute("id", id);
        return "comment-add";
    }

    /**
     * Comment creation function
     * @param user retrieving Authorized User Data Using Spring Security
     * @param request containing a request to create a comment from the user
     * @param model for creating attributes sent to the server as a response
     * @return the 'history look' page
     */
    @PostMapping("/comment/add")
    public String commentAdd(@AuthenticationPrincipal User user,
                             @ModelAttribute AddCommentDtoReq request,
                             Model model) {
        var response = historyService.addComment(user, request);
        if (response.getError() != null) {
            model.addAttribute("errorComment", response.getError());
            model.addAttribute("commentDto", request);
            model.addAttribute("id", request.getId());
            return "comment-add";
        }
        return MessageFormat.format("redirect:/history/{0}/look", request.getId());
    }

}
