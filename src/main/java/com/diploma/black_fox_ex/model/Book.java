package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.dto.book.BookReqDTO;
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

    @Column(name = "f_title", length = 30)
    private String title;

    @Column(name = "f_background_img", length = 30)
    private String filenameBg;

    @Type(type = "text")
    @Column(name = "f_big_text", columnDefinition = "MEDIUMTEXT")
    private String bigText;

    @Enumerated(EnumType.STRING)
    @Column(name = "f_genre", length = 30)
    private Genre genre;

    @Column(name = "f_date_lock")
    private LocalDateTime dateTimeDelete;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_author_id")
    private User author;

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

    public Book(String title, String filenameBg,
                String bigText, Genre genre, User author) {
        this.title = title;
        this.filenameBg = filenameBg;
        this.bigText = bigText;
        this.genre = genre;
        this.author = author;
    }

    public Book(BookReqDTO bookReqDTO, String filenameBg, User author) {
        this(bookReqDTO.getTitle(), filenameBg,
                bookReqDTO.getBigText(), bookReqDTO.getGenre(), author);
    }

    public void updateBook(BookReqDTO bookReqDTO, String filenameBg) {
        this.title = bookReqDTO.getTitle();
        this.filenameBg = filenameBg;
        this.genre = bookReqDTO.getGenre();
        this.bigText = bookReqDTO.getBigText();
    }

    public void addComments(Comment comments) {
        this.comments.add(comments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}