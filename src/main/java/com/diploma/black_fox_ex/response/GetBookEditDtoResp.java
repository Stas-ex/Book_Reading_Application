package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.model.Book;
import lombok.Data;

@Data
public class GetBookEditDtoResp {
    private long id;
    private String title;
    private String backgroundImg;
    private String bigText;
    private String genre;

    public GetBookEditDtoResp(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.backgroundImg = book.getBackgroundImg();
        this.bigText = book.getBigText();
        this.genre = book.getGenre().getTitle();
    }

}