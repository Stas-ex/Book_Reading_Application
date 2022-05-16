package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.model.History;
import lombok.Data;

import java.util.List;

@Data
public class GetHistoryLookDtoResp {
    private long id;
    private String title;
    private String bigText;
    private List<GetCommentsDtoResp> comments;

    public GetHistoryLookDtoResp(History history, List<GetCommentsDtoResp> commentsDto) {
        this.id = history.getId();
        this.title = history.getTitle();
        this.bigText = history.getBigText();
        this.comments = commentsDto;
    }
}
