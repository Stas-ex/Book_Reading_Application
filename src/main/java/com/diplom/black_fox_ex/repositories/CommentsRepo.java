package com.diplom.black_fox_ex.repositories;

import com.diplom.black_fox_ex.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepo extends JpaRepository<Comment, Long> { }
