package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**-----------------------------------------------------------------------------------**/
@Controller
public class AboutController {

    /**-----------------------------------------------------------------------------------**/
    @Autowired
    LogInController logIn;

    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/about")
    public String getHome(Model model) {
        model.addAttribute("user", logIn.logInViewUser());
        return "about";
    }
}
