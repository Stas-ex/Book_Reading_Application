package com.diploma.black_fox_ex.dto;

import lombok.Data;

@Data
@Deprecated
public class AddCommentDtoReq {

    private final long id;
    private final String bigText;
    private final String color;

}
