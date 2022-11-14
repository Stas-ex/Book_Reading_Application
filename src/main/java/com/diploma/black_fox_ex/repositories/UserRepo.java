package com.diploma.black_fox_ex.repositories;

import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String name);

    User findByEmail(String email);

    @Query("select h from User u join u.favorite h where u.id = :id")
    List<Book> findFavoriteBookById(@Param("id") long id);

}
