package com.diploma.black_fox_ex.controllers;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.UserService;

import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

/**
 * This is the class for interacting with the start page.
 */
@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The function for going to the start page
     * @param user Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return the start page
     */
    @GetMapping("/")
    public String getHome(@AuthenticationPrincipal User user, Model model) {
       model.addAttribute("userMenu",userService.getUserMenu(user));
        return "home";
    }
}
