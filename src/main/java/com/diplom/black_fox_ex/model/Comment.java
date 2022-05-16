package com.diplom.black_fox_ex.model;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String text;
    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Comment() {}
    public Comment(String text, String color, User user) {
        this.text = text;
        this.color = color;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && Objects.equals(text, comment.text) && Objects.equals(color, comment.color) && Objects.equals(user, comment.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, color, user);
    }
}
