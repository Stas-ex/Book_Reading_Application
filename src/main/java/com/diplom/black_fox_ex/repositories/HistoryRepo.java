package com.diplom.black_fox_ex.repositories;

import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepo extends JpaRepository<History, Long> {
    List<History> findAllByTags(Tag tag);

    @Query("select h from History h join h.tags t where t.name = :name")
    List<History> findAllByTagName(@Param("name")String name);
}
