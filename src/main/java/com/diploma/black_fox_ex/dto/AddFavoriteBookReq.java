package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class AddFavoriteBookReq {
    private User user;
    private long bookId;

    public AddFavoriteBookReq(User user, long bookId) {
        this.user = user;
        this.bookId = bookId;
    }
}
