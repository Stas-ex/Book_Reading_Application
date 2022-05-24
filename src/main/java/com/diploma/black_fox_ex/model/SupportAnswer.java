package com.diploma.black_fox_ex.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class SupportAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length=1000)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    public SupportAnswer(long id) {
        this.id = id;
    }

    public SupportAnswer(long id, String answer, User user) {
        this.id = id;
        this.answer = answer;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportAnswer that = (SupportAnswer) o;
        return id == that.id && Objects.equals(answer, that.answer) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, user);
    }
}
