package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.model.Role;
import com.diplom.black_fox_ex.request.UserDtoRegDtoRequest;
import com.diplom.black_fox_ex.response.UserRegDtoResponse;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
/**-----------------------------------------------------------------------------------**/

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    UserService userService;

    @Autowired
    LogInController logIn;

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("answer", "");
        model.addAttribute("user", logIn.logInViewUser());
        return "registration";
    }

    @PostMapping("/add")
    public String addNewUser(@RequestParam("username") String username, @RequestParam("email") String email,
                             @RequestParam("password") String password, @RequestParam("age") String age,
                             @RequestParam("info") String info,@RequestParam("img") MultipartFile img,
                             @RequestParam(defaultValue = "") String sex,
                             Model model) throws IOException {
        UserDtoRegDtoRequest dto = new UserDtoRegDtoRequest(username,email,password,age,info,img,sex, Collections.singleton(Role.USER));
        UserRegDtoResponse response = userService.registrationUser(dto);
        if(response.getErrors() != null){
            model.addAttribute("answer",response.getErrors());
            return "registration";
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }

}
