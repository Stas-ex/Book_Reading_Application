package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class DeleteHistoryDtoReq {
    private long id;
    private User user;

    public DeleteHistoryDtoReq(long id, User user) {
        this.id = id;
        this.user = user;
    }
}
