package com.diplom.black_fox_ex.request;

import com.diplom.black_fox_ex.response.UserDto;

public class HelpDeleteAnswerRequest {
    UserDto userDto;
    long id;

    public HelpDeleteAnswerRequest(UserDto userDto, long id) {
        this.userDto = userDto;
        this.id = id;
    }


    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
