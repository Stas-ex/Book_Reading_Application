package com.diploma.black_fox_ex.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateBookDtoReq {

    private String title;
    private String bigText;
    private String genre;
    private MultipartFile imgFile;

    public CreateBookDtoReq(String title, MultipartFile imgFile, String bigText, String genre) {
        this.title = title;
        this.imgFile = imgFile;
        this.bigText = bigText;
        this.genre = genre;
    }
}
