package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.User;
import lombok.Data;

@Data
@Deprecated
public class DeleteFavoriteBookDtoReq {

    private final long bookId;
    private final User user;

}
