package com.diploma.black_fox_ex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor

@Getter
@Setter
@Entity
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ask", length = 2000)
    private String ask;

    @Column(name = "answer", length = 2000)
    private String answer;

    @Column(name = "date")
    private LocalDateTime date;

    public Support(Long id, String ask, String answer) {
        this.id = id;
        this.ask = ask;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Support support = (Support) o;
        return Objects.equals(id, support.id) && Objects.equals(ask, support.ask) && Objects.equals(answer, support.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ask, answer);
    }
}
