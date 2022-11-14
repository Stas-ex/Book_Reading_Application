package com.diploma.black_fox_ex;

import com.diploma.black_fox_ex.dto.book.BookReqDTO;
import com.diploma.black_fox_ex.dto.book.ReferenceBookDTO;
import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Genre;
import com.diploma.black_fox_ex.model.constant.Sex;
import org.springframework.mock.web.MockMultipartFile;

public abstract class AbstractFactoryEntityTest {

    public Book createSmallBook() {
        return new Book("myBookTitle", "book.jpeg", "bigText".repeat(10), Genre.DETECTIVE, new User());
    }

    public BookReqDTO createBookDTO() {
        var img = new MockMultipartFile("file", "IMG".getBytes());
        return new BookReqDTO("myBookTitle",
                "bigText ".repeat(30),
                Genre.DETECTIVE.name(), img
        );
    }

    public ReferenceBookDTO createRefBookDTO(Long id) {
        return new ReferenceBookDTO(
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
