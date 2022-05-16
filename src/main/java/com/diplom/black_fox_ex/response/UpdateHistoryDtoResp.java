package com.diplom.black_fox_ex.response;

import lombok.Data;

@Data
public class UpdateHistoryDtoResp {
    private String error;
    private GetHistoryCardDtoResp historyDto;
}
