package com.diploma.black_fox_ex.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateHistoryDtoReq {
    private long id;
    private String title;
    private String bigText;
    private MultipartFile imgFile;
    private String username;
    private String tag;

    public UpdateHistoryDtoReq(long id, String title, String bigText, MultipartFile imgFile, String username, String tag) {
        this.id = id;
        this.title = title;
        this.bigText = bigText;
        this.imgFile = imgFile;
        this.username = username;
        this.tag = tag;
    }


}
