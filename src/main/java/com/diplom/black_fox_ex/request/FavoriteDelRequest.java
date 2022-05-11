package com.diplom.black_fox_ex.request;

import com.diplom.black_fox_ex.model.User;
import com.diplom.black_fox_ex.response.UserDto;

public class FavoriteDelRequest {
    private long historyId;
    private UserDto user;

    public FavoriteDelRequest(long historyId, UserDto user) {
        this.historyId = historyId;
        this.user = user;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
