package com.diploma.black_fox_ex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_ask", length = 2000)
    private String ask;

    @Column(name = "f_answer", length = 2000)
    private String answer;

    public Support(Long id, String ask, String answer) {
        this.id = id;
        this.ask = ask;
        this.answer = answer;
    }
}
