package com.diploma.black_fox_ex.dto.user;

import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class UserMenuDTO {
    private String username;
    private String imgFile;
    private String email;

    public UserMenuDTO(User user) {
        this.username = user.getUsername();
        this.imgFile = FileDirectories.USER_IMG_DIR.getPath() + user.getImgFile();
        this.email = user.getEmail();
    }
}
