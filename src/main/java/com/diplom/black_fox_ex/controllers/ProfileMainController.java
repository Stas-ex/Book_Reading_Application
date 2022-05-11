package com.diplom.black_fox_ex.controllers;
/**-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.request.UpdateDtoRequest;
import com.diplom.black_fox_ex.response.UserDto;
import com.diplom.black_fox_ex.response.UserUpdateDtoResponse;
import com.diplom.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
/**-----------------------------------------------------------------------------------**/
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileMainController {
    /**-----------------------------------------------------------------------------------**/
    @Autowired
    private UserService userService;

    @Autowired
    LogInController logIn;
    /**-----------------------------------------------------------------------------------**/
    @GetMapping
    public String profileLogIn(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userService.getUserByUsernameView(username);

        if (user == null) {
            model.addAttribute("answer", "Error user is null");
        }

        model.addAttribute("user", user);
        return "/profile/profile-main";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/delete")
    public String blogDelete(Model model) {
        System.out.println("1");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userService.getUserByUsername(username);
        if (user == null) {
            model.addAttribute("answer", "Error user is null");
            return "redirect:/";
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        userService.deleteById(user.getId());
        return "redirect:/";
    }
    /**-----------------------------------------------------------------------------------**/
    @PostMapping("/update")
    public String updateLogIn(@RequestParam("email") String email, @RequestParam("password") String password,
                              @RequestParam("age") String age, @RequestParam("img") MultipartFile img,
                              @RequestParam("info") String info, @RequestParam(value = "telegram", defaultValue = "") String telegramUsername,
                              Model model) {
        UserDto userOld = logIn.logIn();
        UpdateDtoRequest userNew = new UpdateDtoRequest(userOld.getUsername(), email, password, age, info, img, userOld.getSex(),telegramUsername, userOld.getRoles());
        UserUpdateDtoResponse dto = userService.updateUser(userOld, userNew);
        if (dto.getErrors() != null) {
            model.addAttribute("user", userService.getUserByUsernameView(userOld.getUsername()));
            model.addAttribute("answer", dto.getErrors());
            return "/profile/profile-main";
        }
        return "redirect:/profile";
    }
    /**-----------------------------------------------------------------------------------**/
    @GetMapping("/exit")
    public String exitProfile(Model model) {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }
}
