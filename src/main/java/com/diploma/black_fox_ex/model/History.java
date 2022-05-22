package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.request.UpdateHistoryDtoReq;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@ToString
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;

    @Column(length = 300)
    private String backgroundImg;

    @Column(length = 15000)
    private String bigText;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "histories")
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favoriteStories")
    @ToString.Exclude
    private Set<User> userLike = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tag;

    public History() {
    }

    public History(String title, String backgroundImg, String bigText) {
        this.title = title;
        this.backgroundImg = backgroundImg;
        this.bigText = bigText;
    }

    public History(String title, String backgroundImg, String bigText, Tag tag) {
        this(title, backgroundImg, bigText);
        this.tag = tag;
    }

    public History(long id, String title, String backgroundImg, String bigText, Tag tag) {
        this(title, backgroundImg, bigText, tag);
        this.id = id;
    }

    public void addComments(Comment comments) {
        this.comments.add(comments);
    }

    public long getCountLike() {
        return getUserLike().size();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return id == history.id && Objects.equals(title, history.title) && Objects.equals(backgroundImg, history.backgroundImg) && Objects.equals(bigText, history.bigText) && Objects.equals(users, history.users) && Objects.equals(comments, history.comments) && Objects.equals(userLike, history.userLike) && Objects.equals(tag, history.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, backgroundImg, bigText, users, comments, userLike, tag);
    }
}
