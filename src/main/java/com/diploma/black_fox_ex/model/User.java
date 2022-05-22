package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.request.UpdateUserDtoReq;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Column(length = 1000)
    private String info;

    @Column(length = 300)
    private String imgFile;

    private boolean active;
    private String telegramUsername;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_history",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "history_id"))
    @ToString.Exclude
    private List<History> histories = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_favorite",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_id"))
    @ToString.Exclude
    private List<History> favoriteStories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comments_id")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SupportAnswer> supportAnswer = new ArrayList<>();

    public User(String username, String email, String password,
                int age, String sex, String info, String imgFile,
                String telegramUsername, boolean active) {
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

    public User(String username, String email, String password,
                int age, String sex, String info, String imgFile,
                boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.imgFile = imgFile;
        this.active = active;
        supportAnswer = new ArrayList<>();
    }

    public void updateUserByDto(UpdateUserDtoReq userDto) {
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.age = Integer.parseInt(userDto.getAge());
        this.sex = userDto.getSex();
        this.info = userDto.getInfo();
        this.telegramUsername = userDto.getTelegramUsername();
        this.imgFile = userDto.getFileName();
    }

    public void removeSupportAnswer(long id) {
        supportAnswer.removeIf(elem -> elem.getId() == id);
    }


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
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, age, sex, info, imgFile, active, telegramUsername, roles);
    }
}
