package com.diploma.black_fox_ex.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_text")
    private String text;

    @Column(name = "f_color")
    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    public Comment() {
    }

    public Comment(String text, String color, User user) {
        this.text = text;
        this.color = color;
        this.user = user;
    }
}
