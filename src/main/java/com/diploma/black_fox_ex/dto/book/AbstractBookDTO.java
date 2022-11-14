package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public abstract class AbstractBookDTO {

    @NotBlank(message = "valid.book.title.blank")
    @Size(min = 3, max = 20, message = "valid.book.title.range")
    private final String title;

    @NotBlank(message = "valid.book.bigText.blank")
    @Size(min = 100, message = "valid.book.bigText.range")
    private final String bigText;

    @NotNull(message = "valid.book.genre.empty")
    private final Genre genre;

    public AbstractBookDTO(String title, Genre genre, String bigText) {
        this.title = title;
        this.genre = genre;
        this.bigText = bigText;
    }
}