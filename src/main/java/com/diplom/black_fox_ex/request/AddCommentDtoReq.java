package com.diplom.black_fox_ex.request;

import lombok.Data;

@Data
public class AddCommentDtoReq {
    private long id;
    private String bigText;
    private String color;

    public AddCommentDtoReq(long id, String bigText, String color) {
        this.id = id;
        this.bigText = bigText;
        this.color = color;
    }
}
