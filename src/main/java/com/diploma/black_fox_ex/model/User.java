package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "f_username", length = 30)
    private String username;

    @Column(name = "f_email", length = 50)
    private String email;

    @Column(name = "f_password", length = 30)
    private String password;

    @Column(name = "f_age", length = 30)
    private Byte age;

    @Column(name = "f_sex", length = 10)
    private String sex;

    @Column(name = "f_info", length = 1000)
    private String info;

    @Column(name = "f_filename", length = 30)
    private String imgFile;

    @Column(name = "f_active")
    private Boolean active;

    @Column(name = "f_telegram", length = 50)
    private String telegramUsername;

    @Column(name = "f_role", length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_history",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "history_id"))
    private List<History> histories = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_favorite",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_id"))
    private List<History> favoriteStories = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comments_id")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportAnswer> supportAnswer = new ArrayList<>();

    public User(String username, String email, String password, Byte age,
                String sex, String info, String imgFile,
                String telegramUsername) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.imgFile = imgFile;
        this.telegramUsername = telegramUsername;
        this.role = Role.USER;
    }

    public User(UserDto userDto, String filename) {
        this(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(),
                userDto.getAge(), userDto.getSex(), userDto.getInfo(),
                filename, userDto.getTelegram());
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
        return active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
