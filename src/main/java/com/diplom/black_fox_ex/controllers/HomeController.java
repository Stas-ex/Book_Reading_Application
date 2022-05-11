package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**-----------------------------------------------------------------------------------**/
@Controller
public class HomeController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    UserService userService;
    @Autowired
    LogInController logIn;
    /**-----------------------------------------------------------------------------------**/

    @GetMapping("/")
    public String getHome(Model model) {
       model.addAttribute("user",logIn.logInViewUser());
        return "home";
    }
}
