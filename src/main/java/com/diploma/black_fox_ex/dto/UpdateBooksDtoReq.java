package com.diploma.black_fox_ex.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateBooksDtoReq {
    private long id;
    private String title;
    private String bigText;
    private MultipartFile imgFile;
    private String username;
    private String genre;

    public UpdateBooksDtoReq(long id, String title, String bigText, MultipartFile imgFile, String username, String genre) {
        this.id = id;
        this.title = title;
        this.bigText = bigText;
        this.imgFile = imgFile;
        this.username = username;
        this.genre = genre;
    }


}
