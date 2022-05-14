package com.diplom.black_fox_ex.model;

import com.diplom.black_fox_ex.request.UpdateDtoRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String email;
    private String password;
    private int age;
    private String sex;

    @Column(length=1000)
    private String info;

    @Column(length=300)
    private String imgFile;

    private boolean active;
    private String telegramUsername;

    /**
     * targetClass = refers to the desired class ("Role")
     * FetchType.EAGER = all request
     *
     * name = creating an additional table("user_role")
     * joinColumns = a reference to a field in this table
     *
     * we store enum as a string
     */
    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "user_history",
   joinColumns = @JoinColumn(name = "user_id"),
   inverseJoinColumns = @JoinColumn(name = "history_id"))
   private Set<History> histories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_favorite",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_id"))
    private Set<History> favoriteStories = new HashSet<>();

    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comments_id")
    private List<Comments> comments = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "support_id")
    private List<SupportAnswer> supportAnswer = new ArrayList<>();


    public User() {}

    public User(String username, String email, String password, int age, String sex, String info, String imgFile,String telegramUsername, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.imgFile = imgFile;
        this.telegramUsername = telegramUsername;
        this.active = active;
    }

    public User(String username, String email, String password, int age, String sex, String info, String imgFile, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.imgFile = imgFile;
        this.active = active;
    }

    public void updateUserByDto(UpdateDtoRequest userDto) {
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.age = Integer.parseInt(userDto.getAge());
        this.sex = userDto.getSex();
        this.info = userDto.getInfo();
        this.telegramUsername = userDto.getTelegramUsername();
        this.imgFile = userDto.getFileName();
    }

    public void addHistory(History history){
        this.histories.add(history);
        history.getUsers().add(this);
    }
    public void removeHistory(History history){
        this.histories.remove(history);
        history.getUsers().remove(this);
    }

    public void addFavoriteHistory(History history){
        this.favoriteStories.add(history);
        history.getUsers().add(this);
    }
    public void removeFavoriteHistory(History history){
        this.favoriteStories.remove(history);
        history.getUsers().remove(this);
    }

    public void removeSupportAnswer(long id){
        supportAnswer.removeIf(elem -> elem.getId() == id);
    }


    public void addComments(Comments comments){
        this.comments.add(comments);
        comments.setUser(this);
    }
    public void removeComments(Comments comments){
        this.comments.remove(comments);
        comments.setUser(null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImgFile() {
        return imgFile;
    }

    public void setImgFile(String imgFile) {
        this.imgFile = imgFile;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<History> getHistories() {
        return histories;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    public Set<History> getFavoriteStories() {
        return favoriteStories;
    }

    public void setFavoriteStories(Set<History> favoriteStories) {
        this.favoriteStories = favoriteStories;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    //////////////////////////////////////////ДОДЕЛАТЬ

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
