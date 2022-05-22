package com.diploma.black_fox_ex.response;

import lombok.Data;

@Data
public class GetProfileViewHiDtoResp {
    private String error;
    private GetHistoryEditDtoResp historyDto;
}
