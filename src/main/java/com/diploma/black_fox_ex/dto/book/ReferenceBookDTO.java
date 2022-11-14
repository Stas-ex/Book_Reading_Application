package com.diploma.black_fox_ex.dto.book;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;

import static com.diploma.black_fox_ex.io.FileDirectories.BOOK_IMG_DIR;

@Getter
public class ReferenceBookDTO extends AbstractBookDTO {

    private final Long id;
    private final String filenameBg;
    private final Integer likeCount;

    public ReferenceBookDTO(Long id, String title,
                            Genre genre, String filenameBg,
                            String smallText, Integer likeCount) {
        super(title, genre, smallText);
        this.id = id;
        this.likeCount = likeCount;
        this.filenameBg = BOOK_IMG_DIR.getPath() + filenameBg;
    }
}