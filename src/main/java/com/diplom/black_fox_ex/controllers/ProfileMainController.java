package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.request.UpdateDtoRequest;
import com.diplom.black_fox_ex.response.DeleteUserDtoResp;
import com.diplom.black_fox_ex.response.UserProfileDto;
import com.diplom.black_fox_ex.response.UserUpdateDtoResponse;
import com.diplom.black_fox_ex.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




@Controller
@RequestMapping("/profile")
public class ProfileMainController {
    private final UserService userService;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ProfileMainController.class);

    @Autowired
    public ProfileMainController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String profilePage(@AuthenticationPrincipal User user, Model model) {
        var userProfileDto = userService.getUserProfile(user);
        if(userProfileDto == null) {
            model.addAttribute("error", "User mapping error");
            return "/errorPage";
        }
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("userProfile", userProfileDto);
        return "/profile/profile-main";
    }

    @GetMapping("/delete/{username}")
    public String blogDelete(@AuthenticationPrincipal User user, @PathVariable String username, Model model) {
        SecurityContextHolder.getContext().setAuthentication(null);
        DeleteUserDtoResp responseDto = userService.deleteUser(user, username);
        if(responseDto.getError() != null){
            model.addAttribute("errorProfile", "User mapping error");
            return "/profile/profile-main";
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateLogIn(@AuthenticationPrincipal User user,
                              @ModelAttribute UpdateDtoRequest userUpdate,
                              Model model) {
        UserUpdateDtoResponse dto = userService.updateUser(user, userUpdate);
        if (dto.getErrors() != null) {
            model.addAttribute("userProfile", userService.getUserProfile(user));
            model.addAttribute("errorProfile", dto.getErrors());
            return "/profile/profile-main";
        }
        return "redirect:/profile";
    }

    @GetMapping("/exit")
    public String exitProfile(@AuthenticationPrincipal User user, Model model) {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }
}
