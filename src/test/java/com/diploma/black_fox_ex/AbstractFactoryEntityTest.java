package com.diploma.black_fox_ex;

import com.diploma.black_fox_ex.dto.book.BookReqDto;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDto;
import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.model.constant.Sex;
import org.springframework.mock.web.MockMultipartFile;

public abstract class AbstractFactoryEntityTest {

    public Book createSmallBook() {
        return new Book("myBookTitle", "book.jpeg", "bigText".repeat(10), Genre.DETECTIVE, new User());
    }

    public BookReqDto createBookDto() {
        var img = new MockMultipartFile("file", "IMG".getBytes());
        return new BookReqDto("myBookTitle",
                "bigText ".repeat(30),
                Genre.DETECTIVE.name(), img
        );
    }

    public ReferenceBookDto createRefBookDto(Long id) {
        return new ReferenceBookDto(
                id, "myBookTitle", Genre.DETECTIVE, "fileBg.jpg",
                "bigText".repeat(30), 20
        );
    }

    public User createUser() {
        var user = new User("Stanislav", "skuratov-2000@mail.ru",
                "passsword23232", (byte) 33, Sex.MAN, "user.jpeg",
                "@Staseex", "@Staseex"
        );
        user.setId(1L);
        return user;
    }
}
