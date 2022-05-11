package com.diplom.black_fox_ex.controllers;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.response.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
/**-----------------------------------------------------------------------------------**/
@Controller
public class LogInController {
    @Value("${upload.path}")
    private String uploadPath;// Вытаскиваем путь к файлу

    public UserDto logIn(){
        try {
            UserDto userDto = new UserDto((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return userDto;
        } catch (Exception ex) {
            return null;
        }
    }

    public UserDto logInViewUser(){
        try {
            UserDto userDto = new UserDto((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),uploadPath);
            return userDto;
        } catch (Exception ex) {
            return null;
        }
    }
}
