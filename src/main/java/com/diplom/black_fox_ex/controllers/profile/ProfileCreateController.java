package com.diplom.black_fox_ex.controllers.profile;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.service.HistoryService;
import com.diplom.black_fox_ex.request.CreateHistoryDtoReq;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/profile-history")
public class ProfileCreateController {
    private final HistoryService historyService;

    @Autowired
    public ProfileCreateController(HistoryService historyService) {
        this.historyService = historyService;
    }

    //---------------------------------------------------------------------------//
    @GetMapping("/createPage")
    public String profileCreateHistory(@ModelAttribute CreateHistoryDtoReq historyDto, Model model) {
        model.addAttribute("tags", historyService.getAllTag());
        model.addAttribute("btn", "Create");
        model.addAttribute("historyDto", historyDto);
        return "history_editing";
    }

    @PostMapping("/create")
    public String createHistory(@AuthenticationPrincipal User user,
                                @ModelAttribute CreateHistoryDtoReq historyDto,
                                Model model) {
        var response = historyService.createHistory(user, historyDto);
        if (response.getError() != null) {
            model.addAttribute("btn", "Create");
            model.addAttribute("historyDto", historyDto);
            model.addAttribute("tags", historyService.getAllTag());
            model.addAttribute("errorCreateHistory", response.getError());
            return "history_editing";
        }
        return "redirect:/profile-history";
    }
}
