package com.diploma.black_fox_ex.repositories;

import com.diploma.black_fox_ex.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepo extends JpaRepository<History, Long> {

    @Query("SELECT h FROM History h JOIN h.tag t WHERE t.name = :name  GROUP BY h.id")
    List<History> findAllByTagName(@Param("name")String name);

    List<History> findAllByTitle(String title);
}
