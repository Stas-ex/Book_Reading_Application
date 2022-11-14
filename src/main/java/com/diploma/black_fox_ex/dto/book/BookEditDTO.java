package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;

@Getter
public class BookEditDTO extends AbstractBookDTO {

    private User author;

    public BookEditDTO(String title, Genre genre, String bigText, User author) {
        super(title, genre, bigText);
        this.author = author;
    }
}