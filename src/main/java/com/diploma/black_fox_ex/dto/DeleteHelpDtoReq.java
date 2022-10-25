package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class DeleteHelpDtoReq {
    private User user;
    private long id;

    public DeleteHelpDtoReq(User user, long id) {
        this.user = user;
        this.id = id;
    }
}
