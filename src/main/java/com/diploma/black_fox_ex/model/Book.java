package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_title", length = 30)
    private String title;

    @Column(name = "f_background_img", length = 30)
    private String backgroundImg;

    @Column(name = "f_big_text", length = 15000)
    private String bigText;

    @Enumerated(EnumType.STRING)
    @Column(name = "f_genre", length = 30)
    private Genre genre;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_book_added", joinColumns =
    @JoinColumn(name = "fk_user_id", nullable = false), inverseJoinColumns =
    @JoinColumn(name = "fk_book_id", nullable = false))
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_book_favorite", joinColumns =
    @JoinColumn(name = "fk_users_id"), inverseJoinColumns =
    @JoinColumn(name = "fk_book_id"))
    private Set<User> likes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_book_id")
    private List<Comment> comments = new ArrayList<>();

    public Book() {
    }

    public Book(String title, String backgroundImg, String bigText) {
        this.title = title;
        this.backgroundImg = backgroundImg;
        this.bigText = bigText;
    }

    public Book(String title, String backgroundImg, String bigText, Genre genre) {
        this(title, backgroundImg, bigText);
        this.genre = genre;
    }

    public Book(long id, String title, String backgroundImg, String bigText, Genre genre) {
        this(title, backgroundImg, bigText, genre);
        this.id = id;
    }

    public void addComments(Comment comments) {
        this.comments.add(comments);
    }

    public long getCountLike() {
        return getLikes().size();
    }

}