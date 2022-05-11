package com.diplom.black_fox_ex.request;

import org.springframework.web.multipart.MultipartFile;

public class HistoryUpdateDtoReq {
    private long id;
    private String title;
    private String bigText;
    private MultipartFile img;
    private String username;
    private String fileName;

    public HistoryUpdateDtoReq(long id, String title, String bigText, MultipartFile img, String username) {
        this.id = id;
        this.title = title;
        this.bigText = bigText;
        this.img = img;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
