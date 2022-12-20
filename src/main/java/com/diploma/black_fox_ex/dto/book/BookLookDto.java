package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;

@Getter
public class BookLookDto extends BookDto {

    public BookLookDto(String title, Genre genre, String bigText) {
        super(title, genre, bigText.replace("\n","</br>"));
    }
}
