package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class DeleteFavoriteBookDtoReq {
    private long bookId;
    private User user;

    public DeleteFavoriteBookDtoReq(long bookId, User user) {
        this.bookId = bookId;
        this.user = user;
    }
}
