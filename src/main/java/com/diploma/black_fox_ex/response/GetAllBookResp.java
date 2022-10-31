package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllBookResp {

    private List<GetBookCardDtoResp> listDto = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private List<Integer> pageNumbers;

    private String error;


    public void setGenres(List<Genre> genres) {
        genres.forEach(genre -> this.genres.add(genre.name()));
    }

    public void setList(List<Book> list) {
        list.forEach(book -> this.listDto.add(new GetBookCardDtoResp(book)));
    }
}
