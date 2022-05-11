package com.diplom.black_fox_ex.request;

public class CommentAddDtoRequest {
    long idHistory;
    String username;
    String bigText;
    String color;

    public CommentAddDtoRequest(long idHistory, String username, String bigText, String color) {
        this.idHistory = idHistory;
        this.username = username;
        this.bigText = bigText;
        this.color = color;
    }

    public long getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(long idHistory) {
        this.idHistory = idHistory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
