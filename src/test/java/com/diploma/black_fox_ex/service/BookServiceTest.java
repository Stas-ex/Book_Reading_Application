package com.diploma.black_fox_ex.service;

import com.diploma.black_fox_ex.AbstractFactoryEntityTest;
import com.diploma.black_fox_ex.dto.book.BookReqDto;
import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.repositories.BookRepo;
import com.diploma.black_fox_ex.repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookServiceTest extends AbstractFactoryEntityTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepo bookRepo;

    @Mock
    UserRepo userRepo;


    @Test
    void createBook() {
        User user = createUser();
        Book book = createSmallBook();
        var bookDto = Mockito.mock(BookReqDto.class);

        when(userRepo.getById(user.getId())).thenReturn(user);
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        bookService.create(user.getId(), bookDto);

        verify(userRepo, Mockito.times(1)).getById(user.getId());
        verify(bookRepo, Mockito.times(1)).save(any(Book.class));
    }

    @Test
    void updateBook() {
        var bookDto = createBookDto();
        var user = createUser();
        var book = createSmallBook();

        book.setAuthor(user);

        when(bookRepo.findById(666L)).thenReturn(Optional.of(book));
        bookService.update(666L, bookDto, user);

        verify(bookRepo, Mockito.times(1)).save(any(Book.class));
    }

    @Test
    void updateBookAuthorValidationFail() {
        var bookDto = createBookDto();
        var author = createUser();
        var book = createSmallBook();
        book.setAuthor(author);


        when(bookRepo.findById(2L)).thenReturn(Optional.of(book));

        var user = createUser();
        user.setId(22L);

        try {
            bookService.update(2L, bookDto, user);
            fail();
        } catch (RuntimeException ex) {
            assertEquals(ex.getMessage(), "The user %d is not the author of the book.".formatted(user.getId()));
        }

        verify(bookRepo, Mockito.times(0)).save(any(Book.class));
    }

    @Test
    void deleteBook() {
        var book = createSmallBook();
        var user = createUser();

        when(bookRepo.getById(3L)).thenReturn(book);

        try {
            bookService.delete(3L, user);
            fail();
        } catch (RuntimeException ex) {
            assertEquals(ex.getMessage(), "The user %d is not the author of the book.".formatted(user.getId()));
        }

        book.setAuthor(user);
        when(bookRepo.getById(4L)).thenReturn(book);

        bookService.delete(4L, user);

        verify(bookRepo, Mockito.times(1)).save(any(Book.class));
        verify(bookRepo, Mockito.times(0)).delete(any(Book.class));
    }


    @Test
    void getAllBookByGenreValidationError() {
        try {
            bookService.getAllBookByGenre(null, 0);
            fail();
        } catch (RuntimeException ex) {
            assertEquals(ex.getMessage(), "Genre is null");
        }
        try {
            bookService.getAllBookByGenre(Genre.DETECTIVE.name(), 0);
            fail();
        } catch (RuntimeException ex) {
            assertEquals(ex.getMessage(), "Error number page");
        }
    }

    @Test
    void getAllBookByGenre() {
        var bookRef1 = createRefBookDto(1L);
        var bookRef2 = createRefBookDto(2L);
        var genre = Genre.DOCUMENTARY;

        when(bookRepo.getBooksIdByGenre(eq(genre), anyInt(), anyInt())).thenReturn(List.of(1L, 2L));
        when(bookRepo.getReferenceBook(1L)).thenReturn(bookRef1);
        when(bookRepo.getReferenceBook(2L)).thenReturn(bookRef2);

        var pageDto = bookService.getAllBookByGenre(genre.name(), 1);

        assertEquals(pageDto.pageNumbers(), singletonList(1));
        assertEquals(pageDto.elem(), List.of(bookRef1, bookRef2));
    }


    @Test
    void getPageNumbers() {
        int maxBookSize1 = 50;
        int maxBookSize2 = 100;

        int pageSize1 = getPageSizeView(4, 50, maxBookSize1);
        int pageSize2 = getPageSizeView(1, 150, maxBookSize1);
        int pageSize3 = getPageSizeView(7, 200, maxBookSize2);

        final List<Integer> pageNums1 = bookService.getPageNumbers(4, pageSize1);
        final List<Integer> pageNums2 = bookService.getPageNumbers(1, pageSize2);
        final List<Integer> pageNums3 = bookService.getPageNumbers(7, pageSize3);

        assertEquals(pageNums1.size(), 4);
        assertEquals(pageNums1.get(0), 1);
        assertEquals(pageNums1.get(1), 2);
        assertEquals(pageNums1.get(2), 3);
        assertEquals(pageNums1.get(3), 4);

        assertEquals(pageNums2.size(), 3);
        assertEquals(pageNums2.get(0), 1);
        assertEquals(pageNums2.get(1), 2);
        assertEquals(pageNums2.get(2), 3);

        assertEquals(pageNums3.size(), 3);
        assertEquals(pageNums3.get(0), 1);
        assertEquals(pageNums3.get(1), 7);
        assertEquals(pageNums3.get(2), 8);
    }

    private int getPageSizeView(int numPage, int getBookSize, int maxBookSize) {
        return (int) (numPage + Math.ceil((double) getBookSize / maxBookSize));
    }
}