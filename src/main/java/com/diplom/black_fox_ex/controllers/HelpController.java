package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.service.UserService;
import com.diplom.black_fox_ex.request.DeleteHelpDtoReq;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/help")
public class HelpController {
    private final UserService userService;

    @Autowired
    public HelpController(UserService userService) {
        this.userService = userService;
    }

    //---------------------------------------------------------------------------//
    @GetMapping
    public String start(@AuthenticationPrincipal User user, Model model) {
        var response = userService.getAllAnswersSupportByUserDto(user);
        if (response.getErrors() != null)
            model.addAttribute("error", response.getErrors());

        model.addAttribute("answers", response.getAnswers());
        model.addAttribute("user", userService.getUserMenu(user));
        return "help";
    }

    @GetMapping("/{id}/delete")
    public String deleteAnswerSupport(@AuthenticationPrincipal User user, @PathVariable long id, Model model) {
        var request = new DeleteHelpDtoReq(user, id);
        var response = userService.deleteAnswerByUser(request);
        if (response.getErrors() != null) {
            model.addAttribute("error", response.getErrors());
            return "help";
        }
        return "redirect:/help";
    }
}
