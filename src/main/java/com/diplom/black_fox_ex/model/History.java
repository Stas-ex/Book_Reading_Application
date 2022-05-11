package com.diplom.black_fox_ex.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;

    @Column(length=300)
    private String backgroundImg;

    @Column(length=15000)
    private String bigText;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "histories")
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Comments> comments=new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "favoriteStories")
    private Set<User> userLike = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "history_tag",
            joinColumns = @JoinColumn(name = "history_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();



    public History() {}

    public History(String title, String backgroundImg, String bigText) {
        this.title = title;
        this.backgroundImg = backgroundImg;
        this.bigText = bigText;
    }

    public History(long id, String title, String backgroundImg, String bigText) {
        this.id = id;
        this.title = title;
        this.backgroundImg = backgroundImg;
        this.bigText = bigText;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }
    public void removeTag(Tag tag){this.tags.remove(tag);}


    public void addComments(Comments comments){
        this.comments.add(comments);
    }
    public void removeComments(Comments comments){
        this.comments.remove(comments);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public Set<User> getUserLike() {
        return userLike;
    }

    public void setUserLike(Set<User> userLike) {
        this.userLike = userLike;
    }

    public long getCountLike(){
        return  getUserLike().size();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return id == history.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
