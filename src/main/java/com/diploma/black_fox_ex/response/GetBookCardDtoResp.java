package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.model.Book;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class GetBookCardDtoResp {
    private long id;
    private String title;
    private String backgroundImg;
    private List<String> listAuthors = new ArrayList<>();
    private String smallText;
    private long countLike;
    private String genre;

    public GetBookCardDtoResp(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.backgroundImg = FileDirectories.BOOK_IMG.getPath() + book.getBackgroundImg();
        this.countLike = book.getCountLike();
        this.genre = book.getGenre().getTitle();
        book.getUsers().forEach(elem -> listAuthors.add(elem.getUsername()));
        String bigText = book.getBigText();

        if (bigText.length() <= 120)
            this.smallText = bigText.substring(0, book.getBigText().length() - 1);
        else
            this.smallText = bigText.substring(0, 120) + "...";
    }
}



