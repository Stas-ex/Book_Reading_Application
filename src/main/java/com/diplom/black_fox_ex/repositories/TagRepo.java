package com.diplom.black_fox_ex.repositories;

import com.diplom.black_fox_ex.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
