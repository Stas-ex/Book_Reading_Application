package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class DeleteFavoriteHiDtoReq {
    private long historyId;
    private User user;

    public DeleteFavoriteHiDtoReq(long historyId, User user) {
        this.historyId = historyId;
        this.user = user;
    }
}
