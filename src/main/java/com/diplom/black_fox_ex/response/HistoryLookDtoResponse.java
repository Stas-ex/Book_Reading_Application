package com.diplom.black_fox_ex.response;

public class HistoryLookDtoResponse {
    String error;
    String likeActive = "outline-";

    HistoryDto historyDto;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HistoryDto getHistoryDto() {
        return historyDto;
    }

    public void setHistoryDto(HistoryDto historyDto) {
        this.historyDto = historyDto;
    }

    public String getLikeActive() {
        return likeActive;
    }

    public void setLikeActive(String likeActive) {
        this.likeActive = likeActive;
    }
}
