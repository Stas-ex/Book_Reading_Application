package com.diploma.black_fox_ex.controllers.user;

import com.diploma.black_fox_ex.dto.user.UserDto;
import com.diploma.black_fox_ex.model.constant.Sex;
import com.diploma.black_fox_ex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * This is the class for interacting with the registration pageNum.
 */
@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    /**
     * The function for going to the registration pageNum
     *
     * @param userDto that accepts user input
     * @return the 'registration' pageNum
     */
    @GetMapping
    public String registration(@ModelAttribute("userReg") UserDto userDto, Model model) {
        model.addAttribute("genders", Sex.values());
        return "registration";
    }

    /**
     * @param userDto that accepts user input
     * @param model   for creating attributes sent to the server as a response
     * @return to original pageNum in case of error, otherwise to the login pageNum
     */
    @PostMapping
    public String addNewUser(@Valid @ModelAttribute("userReg") UserDto userDto,
                             BindingResult valid,
                             Model model) {
        if (userService.isExistUser(userDto.getUsername())) {
            valid.addError(new ObjectError(
                    "username", "exception.user.already.exist")
            );
        }

        if (valid.hasErrors()) {
            model.addAttribute("genders", Sex.values());
            model.addAttribute("warnings", valid.getAllErrors());
            return "registration";
        }
        userService.register(userDto);
        return "redirect:/login";
    }
}
