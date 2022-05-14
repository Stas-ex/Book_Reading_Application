package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.request.UserDtoRegDtoRequest;
import com.diplom.black_fox_ex.response.UserRegDtoResponse;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

//    @Autowired
//    LogInController logIn;

    @GetMapping
    public String registration(@ModelAttribute("userReg") UserDtoRegDtoRequest dtoRequest, Model model) {
//        model.addAttribute("user", logIn.logInViewUser());
        return "registration";
    }

    @PostMapping("/add")
    public String addNewUser(@ModelAttribute("userReg") UserDtoRegDtoRequest dtoRequest, Model model){
        UserRegDtoResponse dtoResponse = userService.registrationUser(dtoRequest);
        if(dtoResponse.getErrors() != null){
            model.addAttribute("errorReg", dtoResponse.getErrors());
            model.addAttribute("userReg", dtoRequest);
            return "registration";
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }


//    @PostMapping("/add")
//    public String addNewUser(@RequestParam("username") String username, @RequestParam("email") String email,
//                             @RequestParam("password") String password, @RequestParam("age") String age,
//                             @RequestParam("info") String info,@RequestParam("img") MultipartFile img,
//                             @RequestParam(defaultValue = "") String sex,
//                             Model model){
//        var dtoRequest = new UserDtoRegDtoRequest(username,email,password,age,info,img,sex);
//        UserRegDtoResponse dtoResponse = userService.registrationUser(dtoRequest);
//        if(dtoResponse.getErrors() != null){
//            model.addAttribute("answer", dtoResponse.getErrors());
//            return "registration";
//        }
//        SecurityContextHolder.getContext().setAuthentication(null);
//        return "redirect:/login";
//    }

}
