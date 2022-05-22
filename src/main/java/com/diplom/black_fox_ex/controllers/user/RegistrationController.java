package com.diplom.black_fox_ex.controllers.user;

import com.diplom.black_fox_ex.request.RegistrationUserDtoReq;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the registration page.
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {this.userService = userService;}

    /**
     * The function for going to the registration page
     * @param userDto that accepts user input
     * @return the 'registration' page
     */
    @GetMapping
    public String registration(@ModelAttribute("userReg") RegistrationUserDtoReq userDto) {
        return "registration";
    }

    /**
     *
     * @param userDto that accepts user input
     * @param model for creating attributes sent to the server as a response
     * @return to original page in case of error, otherwise to the login page
     */
    @PostMapping("/add")
    public String addNewUser(@ModelAttribute("userReg") RegistrationUserDtoReq userDto, Model model) {
        var dtoResponse = userService.registrationUser(userDto);
        if (dtoResponse.getError() != null) {
            model.addAttribute("errorReg", dtoResponse.getError());
            model.addAttribute("userReg", userDto);
            return "registration";
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }
}
