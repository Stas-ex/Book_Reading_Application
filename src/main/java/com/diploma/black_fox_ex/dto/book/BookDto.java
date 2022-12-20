package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BookDto {

    @NotBlank(message = "exception.book.title.blank")
    @Size(min = 3, max = 20, message = "exception.book.title.range")
    private final String title;

    @NotNull(message = "exception.book.genre.empty")
    private final Genre genre;

    @NotBlank(message = "exception.book.bigText.blank")
    @Size(min = 100, message = "exception.book.main-text.range")
    private final String bigText;

}