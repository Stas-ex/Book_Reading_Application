package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.constant.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


@Data
public class UpdateUserDtoReq {
    private String username;
    private String email;
    private String password;
    private Byte age;
    private String sex;
    private String info;
    private MultipartFile imgFile;
    private boolean active;
    private String fileName;
    private String telegramUsername;
    private Set<Role> roles;


    public UpdateUserDtoReq(String username, String email, String password, byte age, String info,
                            MultipartFile img, String sex, String telegramUsername) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.info = info;
        this.imgFile = img;
        this.active = true;
        this.telegramUsername = telegramUsername;
        this.sex = sex;
    }
}
