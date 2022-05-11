package com.diplom.black_fox_ex.response;

import java.util.List;

public class FavoriteGetAllUserResponse {
    private String error;
    private List<HistoryDto> listDto;

    public List<HistoryDto> getListDto() {
        return listDto;
    }

    public void setListDto(List<HistoryDto> listDto) {
        this.listDto = listDto;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
