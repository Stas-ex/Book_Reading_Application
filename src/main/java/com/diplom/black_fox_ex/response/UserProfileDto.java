package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.model.User;
import lombok.Data;

@Data
public class UserProfileDto {
    private String username;
    private String email;
    private String password;
    private int age;
    private String sex;
    private String info;
    private String imgFile;
    private String telegramUsername;


    public UserProfileDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.sex = user.getSex();
        this.info = user.getInfo();
        this.imgFile = FileDirectories.USER_IMG.getPath() + user.getImgFile();
        this.telegramUsername = user.getTelegramUsername();
    }
}
