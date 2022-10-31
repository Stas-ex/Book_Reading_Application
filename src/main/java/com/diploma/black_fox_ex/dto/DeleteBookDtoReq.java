package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class DeleteBookDtoReq {

    private User user;
    private long id;

    public DeleteBookDtoReq(long id, User user) {
        this.id = id;
        this.user = user;
    }
}
