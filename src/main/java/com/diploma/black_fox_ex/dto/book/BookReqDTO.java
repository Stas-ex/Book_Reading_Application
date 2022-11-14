package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BookReqDTO extends AbstractBookDTO {

    private final MultipartFile imgFile;

    public BookReqDTO(String title, String bigText, String genre, MultipartFile imgFile) {
        super(title, genre == null ? null : Genre.valueOf(genre), bigText);
        this.imgFile = imgFile;
    }
}