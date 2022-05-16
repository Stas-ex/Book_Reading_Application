package com.diplom.black_fox_ex.controllers.profile;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.request.DeleteHistoryDtoReq;
import com.diplom.black_fox_ex.request.UpdateHistoryDtoReq;
import com.diplom.black_fox_ex.service.HistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/profile-history")
public class ProfileHistoriesController {
    private final HistoryService historyService;

    @Autowired
    public ProfileHistoriesController(HistoryService historyService) {
        this.historyService = historyService;
    }

    //---------------------------------------------------------------------------//
    @GetMapping
    public String viewPage(@AuthenticationPrincipal User user, Model model) {
        var historyDtoResp = historyService.getAllHistoryByUser(user);
        if (historyDtoResp.getError() != null)
            model.addAttribute("errorView", historyDtoResp.getError());
        else
            model.addAttribute("histories", historyDtoResp.getHistoryDto());
        return "/profile/profile-viewHistory";
    }

    @GetMapping("/{id}/editPage")
    public String editingHistoryPage(@AuthenticationPrincipal User user,
                                     @PathVariable long id, Model model) {
        var response = historyService.getHistory(user, id);
        if (response.getError() != null) {
            return "redirect:/profile-history";
        }

        model.addAttribute("btn", "Update");
        model.addAttribute("tags", historyService.getAllTag());
        model.addAttribute("historyDto", response.getHistoryDto());
        return "history_editing";
    }

    @PostMapping("/{id}/update")
    public String updateHistory(@AuthenticationPrincipal User user,
                                @ModelAttribute UpdateHistoryDtoReq historyDto, Model model) {
        var response = historyService.updateHistory(user, historyDto);
        if (response.getError() != null) {
            model.addAttribute("historyDto", historyDto);
            model.addAttribute("btn", "Update");
            model.addAttribute("tags", historyService.getAllTag());
            model.addAttribute("id", historyDto.getId());
            model.addAttribute("errorCreateHistory", response.getError());
            return "history_editing";
        }
        return "redirect:/profile-history";
    }

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
