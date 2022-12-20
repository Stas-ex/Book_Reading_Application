package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;

@Getter
public class BookEditDto extends BookDto {

    private final User author;

    public BookEditDto(String title, Genre genre, String bigText, User author) {
        super(title, genre, bigText);
        this.author = author;
    }
}