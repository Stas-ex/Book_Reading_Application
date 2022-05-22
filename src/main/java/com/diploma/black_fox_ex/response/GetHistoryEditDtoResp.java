package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.model.History;
import lombok.Data;

@Data
public class GetHistoryEditDtoResp {
    private long id;
    private String title;
    private String backgroundImg;
    private String bigText;
    private String tag;

    public GetHistoryEditDtoResp(History history) {
        this.id = history.getId();
        this.title = history.getTitle();
        this.backgroundImg = history.getBackgroundImg();
        this.bigText = history.getBigText();
        this.tag = history.getTag().getName();
    }
}
