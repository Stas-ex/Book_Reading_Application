package com.diplom.black_fox_ex.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateHistoryDtoReq {
    private String title;
    private MultipartFile imgFile;
    private String bigText;
    private String tag;
    private String fileName;

    public CreateHistoryDtoReq(String title, MultipartFile imgFile, String bigText, String tag, String fileName) {
        this.title = title;
        this.imgFile = imgFile;
        this.bigText = bigText;
        this.tag = tag;
        this.fileName = fileName;
    }
}
