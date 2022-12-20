package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.model.constant.Genre;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "background_img")
    private String img;

    @Type(type = "text")
    @Column(name = "big_text", columnDefinition = "MEDIUMTEXT")
    private String bigText;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "date_lock")
    private LocalDateTime dateTimeDeletion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_author_id")
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_book_favorite",
            joinColumns = @JoinColumn(name = "fk_users_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_book_id"))
    private List<User> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_book_id")
    private List<Comment> comments = new ArrayList<>();

    public Book() {}

    public Book(String title,
                String img,
                String bigText,
                Genre genre,
                User author) {
        this.title = title;
        this.img = img;
        this.bigText = bigText;
        this.genre = genre;
        this.author = author;
    }

    public void addComments(Comment comments) {
        this.comments.add(comments);
    }

    public void addLike(User user){
        this.likes.add(user);
        user.getFavorite().add(this);
    }

    public void removeLike(User user){
        this.likes.remove(user);
        user.getFavorite().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}