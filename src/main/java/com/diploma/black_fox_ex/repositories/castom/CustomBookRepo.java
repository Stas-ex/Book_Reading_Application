package com.diploma.black_fox_ex.repositories.castom;

import com.diploma.black_fox_ex.dto.book.BookEditDTO;
import com.diploma.black_fox_ex.dto.book.BookLookDTO;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDTO;
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

    ReferenceBookDTO getReferenceBook(long id);

    int getPageCountByBookId(Long bookId, int maxSymbols);

    BookEditDTO getBookEditDTO(Long bookId);

    BookLookDTO getSplitBookDTO(Long bookId, int fromSymbols, int toSymbols);

}