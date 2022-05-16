package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.request.RegistrationUserDtoReq;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {this.userService = userService;}

    //---------------------------------------------------------------------------//
    @GetMapping
    public String registration(@ModelAttribute("userReg") RegistrationUserDtoReq dtoRequest) {
        return "registration";
    }

    @PostMapping("/add")
    public String addNewUser(@ModelAttribute("userReg") RegistrationUserDtoReq dtoRequest, Model model) {
        var dtoResponse = userService.registrationUser(dtoRequest);
        if (dtoResponse.getErrors() != null) {
            model.addAttribute("errorReg", dtoResponse.getErrors());
            model.addAttribute("userReg", dtoRequest);
            return "registration";
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }
}
