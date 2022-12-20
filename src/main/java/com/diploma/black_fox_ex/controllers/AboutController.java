package com.diploma.black_fox_ex.controllers;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the class for interacting with the "about author" pageNum.
 */
@Controller
@RequestMapping("/about")
@RequiredArgsConstructor
public class AboutController {

    private final UserService userService;

    /**
     *
     * @param user Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return the "about author" pageNum
     */
    @GetMapping
    public String getPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("userMenu", userService.getUserMenu(user));
        return "about";
    }
}
