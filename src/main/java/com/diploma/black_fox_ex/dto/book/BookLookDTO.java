package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;

public class BookLookDTO  extends AbstractBookDTO{

    public BookLookDTO(String title, Genre genre, String bigText) {
        super(title, genre, bigText.replace("\n","</br>"));
    }
}
