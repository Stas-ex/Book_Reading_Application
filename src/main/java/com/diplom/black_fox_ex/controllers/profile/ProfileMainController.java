package com.diplom.black_fox_ex.controllers.profile;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.request.UpdateUserDtoReq;
import com.diplom.black_fox_ex.service.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This is the class for interacting with the "user profile" page.
 */
@Controller
@RequestMapping("/profile")
public class ProfileMainController {
    private final UserService userService;

    @Autowired
    public ProfileMainController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Function to go to the profile page
     * @param user Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return the 'user profile' page otherwise error page
     */
    @GetMapping
    public String profilePage(@AuthenticationPrincipal User user, Model model) {
        var userProfileDto = userService.getUserProfile(user);
        if (userProfileDto == null) {
            model.addAttribute("error", "User mapping error");
            return "/errorPage";
        }
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("userProfile", userProfileDto);
        return "/profile/profile-main";
    }

    /**
     * Function to update the user
     * @param user Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @param userUpdate containing a request from the user
     * @return to 'user profile' page and possible errors
     */
    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user,
                         @ModelAttribute UpdateUserDtoReq userUpdate,
                         Model model) {
        var dto = userService.updateUser(user, userUpdate);
        if (dto.getErrors() != null) {
            model.addAttribute("userProfile", userService.getUserProfile(user));
            model.addAttribute("errorProfile", dto.getErrors());
            return "/profile/profile-main";
        }
        return "redirect:/profile";
    }

    /**
     * Function to delete the user
     * @param user Retrieving Authorized User Data Using Spring Security
     * @param username contains the username of the user being deleted
     * @param model for creating attributes sent to the server as a response
     * @return the start page otherwise 'user profile' page
     */
    @GetMapping("/delete/{username}")
    public String delete(@AuthenticationPrincipal User user, @PathVariable String username, Model model) {
        SecurityContextHolder.getContext().setAuthentication(null);
        var responseDto = userService.deleteUser(user, username);
        if (responseDto.getError() != null) {
            model.addAttribute("errorProfile", "User mapping error");
            return "/profile/profile-main";
        }
        return "redirect:/";
    }

    /**
     * User account logout function
     * @return the start page
     */
    @GetMapping("/exit")
    public String logOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }
}
