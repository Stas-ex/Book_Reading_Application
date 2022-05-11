package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.model.Role;
import com.diplom.black_fox_ex.model.User;
import lombok.Data;
import java.util.*;

@Data
public class UserDto {

    private long id;
    private String username;
    private String email;
    private String password;
    private int age;
    private String sex;
    private String info;
    private String imgFile;
    private boolean active;
    private String telegramUsername;
    private Set<Role> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.sex = user.getSex();
        this.info = user.getInfo();
        this.imgFile =user.getImgFile();
        this.active = user.isActive();
        this.telegramUsername = user.getTelegramUsername();
        this.roles = user.getRoles();
    }

    public UserDto(User user, String uploadPath) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.sex = user.getSex();
        this.info = user.getInfo();
        this.imgFile = uploadPath + user.getImgFile();
        this.active = user.isActive();
        this.telegramUsername = user.getTelegramUsername();
        this.roles = user.getRoles();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public String getImgFile() {
        return imgFile;
    }

    public void setImgFile(String imgFile) {
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

}
