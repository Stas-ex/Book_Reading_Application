package com.diploma.black_fox_ex.dto;
import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
public class AddFavoriteHiReq {
    private User user;
    private long historyId;

    public AddFavoriteHiReq(User user, long historyId) {
        this.user = user;
        this.historyId = historyId;
    }
}
