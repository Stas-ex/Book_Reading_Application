package com.diplom.black_fox_ex.repositories;

import com.diplom.black_fox_ex.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepo extends JpaRepository<Comments, Long> { }
