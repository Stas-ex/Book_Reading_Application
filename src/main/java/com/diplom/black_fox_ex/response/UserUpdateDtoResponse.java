package com.diplom.black_fox_ex.response;
import com.diplom.black_fox_ex.model.User;

public class UserUpdateDtoResponse {
        private String errors;

    public UserUpdateDtoResponse() {}

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
