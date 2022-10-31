package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.model.Book;
import lombok.Data;

import java.util.List;

@Data
public class GetBookLookDtoResp {

    private List<GetCommentsDtoResp> comments;

    private long id;
    private String title;
    private String bigText;

    public GetBookLookDtoResp(Book book, List<GetCommentsDtoResp> commentsDto) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.bigText = book.getBigText()
                .replaceAll("\\r", "<br>")
                .replaceAll("\\n", "<br>");
        this.comments = commentsDto;
    }
}
