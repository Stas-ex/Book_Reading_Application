package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;

import static com.diploma.black_fox_ex.io.ImgDirectories.BOOK_IMG_DIR;

@Getter
public class ReferenceBookDto extends BookDto {

    private final Long id;
    private final String img;
    private final Integer likeCount;

    public ReferenceBookDto(Long id,
                            String title,
                            Genre genre,
                            String img,
                            String smallText,
                            Integer likeCount) {
        super(title, genre, smallText);
        this.id = id;
        this.likeCount = likeCount;
        this.img = BOOK_IMG_DIR.getPath() + img;
    }
}