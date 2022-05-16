package com.diplom.black_fox_ex.response;

import lombok.Data;
import java.util.List;

@Data
public class GetProfileViewHiAllDtoResp {
    private String error;
    private List<GetHistoryCardDtoResp> historyDto;
}
