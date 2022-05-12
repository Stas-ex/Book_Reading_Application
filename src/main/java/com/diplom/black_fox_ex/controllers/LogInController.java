package com.diplom.black_fox_ex.controllers;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.response.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class LogInController {
    @Value("${upload.path}")
    private String uploadPath;

    public UserDto logIn() {
        try {
            return new UserDto((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception ex){
            return null;
        }
    }

    public UserDto logInViewUser() {
        try {
            return new UserDto((User) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal()
                    , uploadPath);
        } catch (Exception ex) {
            return null;
        }
    }
}
