package com.diploma.black_fox_ex.repositories;

import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.Support;
import com.diploma.black_fox_ex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String name);

    User findByEmail(String email);

    @Query("select h from User u join u.favorite h where u.id = :id")
    List<Book> findFavoriteBookById(@Param("id") long id);

    @Query("select h from User u join u.books h where u.id = :id")
    List<Book> findAllById(@Param("id") long id);

    @Query("select h from User u join u.books h where u.id = :idUser and h.id = :idBook")
    Book findBookById(@Param("idUser") long idUser, @Param("idBook") long idBook);

    @Query("SELECT s FROM User u join u.support s where u.id = :id")
    List<Support> findSupportAnswerById(@Param("id") long id);

}
