package com.diploma.black_fox_ex.response;

import lombok.Data;

@Data
public class GetBookLookPageDtoResp {
    private String error;
    private String likeActive = "outline-";
    private GetBookLookDtoResp bookDto;
}
