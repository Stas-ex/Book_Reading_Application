package com.diplom.black_fox_ex.response;

import java.util.List;

public class ProfileViewHiAllDtoResponse {
    String error;
    List<HistoryDto> historyDto;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<HistoryDto> getHistoryDto() {
        return historyDto;
    }

    public void setHistoryDto(List<HistoryDto> historyDto) {
        this.historyDto = historyDto;
    }
}
