package com.diploma.black_fox_ex.controllers.user;

import com.diploma.black_fox_ex.dto.user.UserDTO;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Sex;
import com.diploma.black_fox_ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * This is the class for interacting with the "user profile" pageNum.
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
     * Function to go to the profile pageNum
     *
     * @param user  Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return the 'user profile' pageNum otherwise error pageNum
     */
    @GetMapping
    public String profilePage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("genders", Sex.values());
        model.addAttribute("userMenu", userService.getUserMenu(user));
        model.addAttribute("userProfile", userService.getUserProfile(user));
        return "/profile/profile-main";
    }

    /**
     * Function to update the user
     *
     * @param user  Retrieving Authorized User Data Using Spring Security
     * @param model for creating attributes sent to the server as a response
     * @return to 'user profile' pageNum and possible errors
     */
    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user,
                         @Valid @ModelAttribute("userProfile") UserDTO userDto,
                         BindingResult valid,
                         Model model) {
        model.addAttribute("genders", Sex.values());

        if (valid.hasErrors()) {
            model.addAttribute("userProfile", userService.getUserProfile(user));
            model.addAttribute("warnings", valid.getAllErrors());
            return "/profile/profile-main";
        }

        userService.updateUser(user, userDto);
        return "redirect:/profile";
    }

    /**
     * Function to delete the user
     *
     * @param user Retrieving Authorized User Data Using Spring Security
     * @return the start pageNum otherwise 'user profile' pageNum
     */
    @GetMapping("/delete/me")
    public String delete(@AuthenticationPrincipal User user) {
        SecurityContextHolder.getContext().setAuthentication(null);
        userService.deleteUser(user);
        return "redirect:/";
    }

    /**
     * User account logout function
     *
     * @return the start pageNum
     */
    @GetMapping("/exit")
    public String logOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }
}
