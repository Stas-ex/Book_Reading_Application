package com.diploma.black_fox_ex.repositories;
/*-----------------------------------------------------------------------------------**/
import com.diploma.black_fox_ex.model.Comment;
import com.diploma.black_fox_ex.model.History;
import com.diploma.black_fox_ex.model.SupportAnswer;
import com.diploma.black_fox_ex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
/**-----------------------------------------------------------------------------------**/

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String name);
    @Query("select u from User u where u.id = :id")
    User findById(long id);

    @Query("select h from User u join u.favoriteStories h where u.id = :id")
    List<History> findFavoriteHistoryById(@Param("id")long id);

    @Query("select h from User u join u.histories h where u.id = :id")
    List<History> findAllById(@Param("id") long id);

    @Query("select h from User u join u.histories h where u.id = :idUser and h.id = :idHistory")
    History findHistoryById(@Param("idUser")long idUser, @Param("idHistory") long idHistory);

    @Query("select c from User u join u.comments c where u.id = :id")
    List<Comment> findAllCommentsByUserId(@Param("id")long idUser);

    @Query("SELECT s FROM User u join u.supportAnswer s where u.id = :id")
    List<SupportAnswer> findSupportAnswerById(@Param("id")long id);
}
