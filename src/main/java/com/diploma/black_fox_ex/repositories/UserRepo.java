package com.diploma.black_fox_ex.repositories;

import com.diploma.black_fox_ex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    //optional
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);

//    @Query("select h from User u join u.favorite h where u.id = :userId")
//    List<Book> findFavoriteBooksUsingUserId(@Param("id") long userId);

}
