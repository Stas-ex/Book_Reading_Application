package com.diplom.black_fox_ex.request;

import org.springframework.web.multipart.MultipartFile;

public class HistoryCreateDtoReq {
    String nameHistory;
    MultipartFile fileBackgroundName;
    String bigText;
    String username;
    String fileName;
    String tagId;

    public HistoryCreateDtoReq(String nameHistory, MultipartFile fileBackgroundName, String bigText,String tagId, String username) {
        this.nameHistory = nameHistory;
        this.fileBackgroundName = fileBackgroundName;
        this.bigText = bigText;
        this.tagId = tagId;
        this.username = username;
    }

    public String getNameHistory() {
        return nameHistory;
    }

    public void setNameHistory(String nameHistory) {
        this.nameHistory = nameHistory;
    }

    public MultipartFile getFileBackgroundName() {
        return fileBackgroundName;
    }

    public void setFileBackgroundName(MultipartFile fileBackgroundName) {
        this.fileBackgroundName = fileBackgroundName;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
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

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
