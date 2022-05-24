package com.diploma.black_fox_ex.request;

import com.diploma.black_fox_ex.model.Role;
import org.springframework.web.multipart.MultipartFile;
import java.util.Set;
import lombok.Data;



@Data
public class UpdateUserDtoReq {
        private String username;
        private String email;
        private String password;
        private String age;
        private String sex;
        private String info;
        private MultipartFile imgFile;
        private boolean active;
        private String fileName;
        private String telegramUsername;
        private Set<Role> roles;


    public UpdateUserDtoReq(String username, String email, String password, String age, String info,
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
