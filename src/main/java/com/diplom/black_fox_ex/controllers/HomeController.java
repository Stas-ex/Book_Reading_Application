package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHome(@AuthenticationPrincipal User user, Model model) {
       model.addAttribute("userMenu",userService.getUserMenu(user));
        return "home";
    }
}
