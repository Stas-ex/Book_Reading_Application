package com.diplom.black_fox_ex.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
    @ToString.Exclude
    private Set<History> histories;

    public Tag(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(histories, tag.histories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, histories);
    }
}
