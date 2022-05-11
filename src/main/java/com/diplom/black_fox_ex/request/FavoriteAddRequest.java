package com.diplom.black_fox_ex.request;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.response.UserDto;

public class FavoriteAddRequest {
    private UserDto user;
    private long idHistory;

    public FavoriteAddRequest(UserDto user, long idHistory) {
        this.user = user;
        this.idHistory = idHistory;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public long getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(long idHistory) {
        this.idHistory = idHistory;
    }
}
