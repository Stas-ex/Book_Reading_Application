package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.service.UserService;
import com.diplom.black_fox_ex.model.User;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * This is the class for interacting with the "about author" page.
 */
@Controller
public class AboutController {
    private final UserService userService;

    @Autowired
    public AboutController(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * @param user Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return the "about author" page
     */
    @GetMapping("/about")
    public String getHome(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("userMenu",userService.getUserMenu(user));
        return "about";
    }
}
