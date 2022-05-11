package com.diplom.black_fox_ex.repositories;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.SupportAnswer;
import com.diplom.black_fox_ex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**-----------------------------------------------------------------------------------**/

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String name);

    @Query("select h from User u join u.favoriteStories h where u.id = :id")
    List<History> findFavoriteHistoryById(@Param("id")long id);

    @Query("select h from User u join u.histories h where u.username = :username")
    List<History> findAllByUsername(@Param("username")String username);

    @Query("select h from User u join u.histories h where u.username = :username and h.id = :id")
    History findAllByUsernameAndId(@Param("username")String username, @Param("id") long id);

    @Query("SELECT s FROM User u join u.supportAnswer s where u.id = :id")
    List<SupportAnswer> findSupportAnswerById(@Param("id")long id);

}
