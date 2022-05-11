package com.diplom.black_fox_ex.request;

import com.diplom.black_fox_ex.model.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class UpdateDtoRequest {
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


    public UpdateDtoRequest(String username, String email, String password, String age, String info, MultipartFile img,String sex, String telegramUsername, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.info = info;
        this.imgFile = img;
        this.active = true;
        this.roles = roles;
        this.telegramUsername = telegramUsername;
        this.sex = sex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public MultipartFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(MultipartFile imgFile) {
        this.imgFile = imgFile;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }
}
