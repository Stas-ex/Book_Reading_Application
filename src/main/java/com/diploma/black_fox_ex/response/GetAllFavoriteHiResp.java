package com.diploma.black_fox_ex.response;

import lombok.Data;

import java.util.List;

@Data
public class GetAllFavoriteHiResp {

    private List<GetBookCardDtoResp> listDto;
    private String error;
}
