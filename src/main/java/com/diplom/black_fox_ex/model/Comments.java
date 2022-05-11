package com.diplom.black_fox_ex.model;
import javax.persistence.*;
import java.util.Objects;


@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String text;
    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;


    public Comments() {}

    public Comments(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public Comments(String text, String color) {
        this.text = text;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return id == comments.id && Objects.equals(text, comments.text) && Objects.equals(color, comments.color) && Objects.equals(user, comments.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, color, user);
    }
}
