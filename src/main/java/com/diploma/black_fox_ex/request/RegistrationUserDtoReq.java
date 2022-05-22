package com.diploma.black_fox_ex.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegistrationUserDtoReq {
    private String username;
    private String email;
    private String password;
    private String age;
    private String sex;
    private String info;
    private MultipartFile img;
    private String fileName;
    private boolean active;

    public RegistrationUserDtoReq(String username, String email, String password, String age, String info, MultipartFile img, String sex) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.img = img;
        this.active = true;
    }
}
