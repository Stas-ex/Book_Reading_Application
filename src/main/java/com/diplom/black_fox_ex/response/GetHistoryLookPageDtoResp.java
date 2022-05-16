package com.diplom.black_fox_ex.response;

import lombok.Data;

@Data
public class GetHistoryLookPageDtoResp {
    private String error;
    private String likeActive = "outline-";
    private GetHistoryLookDtoResp historyDto;
}
