package com.diplom.black_fox_ex.request;

public class FavoriteGetAllUserRequest {
    private long userId;

    public FavoriteGetAllUserRequest(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
