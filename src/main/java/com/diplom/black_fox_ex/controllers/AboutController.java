package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {
    private final UserService userService;

    @Autowired
    public AboutController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/about")
    public String getHome(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("userMenu",userService.getUserMenu(user));
        return "about";
    }
}
