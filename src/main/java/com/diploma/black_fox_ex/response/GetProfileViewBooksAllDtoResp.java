package com.diploma.black_fox_ex.response;

import lombok.Data;

import java.util.List;

@Data
public class GetProfileViewBooksAllDtoResp {
    private String error;
    private List<GetBookCardDtoResp> booksDto;
}
