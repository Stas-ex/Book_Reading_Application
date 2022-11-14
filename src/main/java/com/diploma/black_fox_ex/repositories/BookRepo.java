package com.diploma.black_fox_ex.repositories;

import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.repositories.castom.CustomBookRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book, Long>, CustomBookRepo { }