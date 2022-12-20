package com.diploma.black_fox_ex.repositories.castom;

import com.diploma.black_fox_ex.dto.book.BookEditDto;
import com.diploma.black_fox_ex.dto.book.BookLookDto;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDto;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;

import java.util.List;
import java.util.Set;

public interface CustomBookRepo {

    List<Long> getBooksIdByUser(User user, int skipRows, int sizeLimit);

    List<Long> getBooksIdByGenre(Genre genre, int skipRows, int sizeLimit);

    List<Long> getFavoriteBooksId(User user, int skipRows, int sizeLimit);

    List<Long> getBooksIdAllGenres(int skipRows, int sizeLimit);

    Set<Long> getLikesIdByBookId(Long id);

    ReferenceBookDto getReferenceBook(long id);

    int getPageCountByBookId(Long bookId, int maxSymbols);

    BookEditDto getBookEditDto(Long bookId);

    BookLookDto getSplitBookDto(Long bookId, int fromSymbols, int toSymbols);

}