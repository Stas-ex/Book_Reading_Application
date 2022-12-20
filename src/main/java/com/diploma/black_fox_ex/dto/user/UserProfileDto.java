package com.diploma.black_fox_ex.dto.user;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
public class UserProfileDto {
    @NotBlank(message = "exception.user.username.empty")
    @Size(min = 3, max = 25, message = "exception.user.username.wrong")
    private final String username;

    @NotBlank(message = "exception.user.email.empty")
    @Email(message = "exception.user.email.wrong")
    private final String email;


    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,35})", message = "exception.user.password.wrong")
    private String password;

    @NotNull(message = "exception.user.age.empty")
    @Range(min = 3, max = 110, message = "exception.user.age.wrong")
    private final Number age;

    @NotBlank(message = "exception.user.sex.empty")
    private final String sex;

    @NotBlank(message = "exception.user.info.empty")
    @Size(min = 5, max = 1000, message = "exception.user.info.wrong")
    private final String info;


    @Pattern(regexp = ".*\\B@(?=\\w{5,32}\\b)[a-zA-Z0-9]+(?:_[a-zA-Z0-9]+)*.*", message = "exception.user.telegram.wrong")
    private String telegram;

    private final MultipartFile img;

    private String filename;

    public UserProfileDto(String username,
                   String email,
                   String password,
                   Number age,
                   String sex,
                   String info,
                   String telegram,
                   MultipartFile img) {
        this.username = username;
        setPassword(password);
        setTelegram(telegram);
        this.email = email;
        this.info = info;
        this.age = age;
        this.sex = sex;
        this.img = img;
    }

    public void setPassword(String password) {
        if(!StringUtils.isBlank(password)) {
            this.password = password;
        }
    }

    public void setTelegram(String telegram) {
        if(!StringUtils.isBlank(telegram)) {
            this.telegram = telegram;
        }
    }
}
