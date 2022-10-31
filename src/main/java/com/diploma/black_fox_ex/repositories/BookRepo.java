package com.diploma.black_fox_ex.repositories;

import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.constant.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findByGenre(Genre genre);

    List<Book> findAllByTitle(String title);

}