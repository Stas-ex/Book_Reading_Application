package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.dto.UpdateUserDtoReq;
import com.diploma.black_fox_ex.dto.UserDto;
import com.diploma.black_fox_ex.model.constant.Role;
import com.diploma.black_fox_ex.model.constant.Sex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "f_active")
    private boolean active;

    @Column(name = "f_username", length = 30)
    private String username;

    @Column(name = "f_email", length = 30)
    private String email;

    @Column(name = "f_password", length = 200)
    private String password;

    @Column(name = "f_age")
    private Byte age;

    @Enumerated(EnumType.STRING)
    @Column(name = "f_sex", length = 30)
    private Sex sex;

    @Column(name = "f_info", length = 1000)
    private String info;

    @Column(name = "f_filename", length = 30)
    private String imgFile;

    @Column(name = "f_telegram", length = 30)
    private String telegramUsername;

    @Enumerated(EnumType.STRING)
    @Column(name = "f_role", length = 30)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_book_added", joinColumns =
    @JoinColumn(name = "fk_user_id", nullable = false), inverseJoinColumns =
    @JoinColumn(name = "fk_book_id", nullable = false))
    private List<Book> books = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_book_favorite", joinColumns =
    @JoinColumn(name = "fk_users_id"), inverseJoinColumns =
    @JoinColumn(name = "fk_book_id"))
    private List<Book> favorite = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_support", joinColumns =
    @JoinColumn(name = "fk_support_id"), inverseJoinColumns =
    @JoinColumn(name = "fk_user_id"))
    private List<Support> support = new ArrayList<>();

    public User(String username, String email, String password, Byte age,
                Sex sex, String info, String imgFile,
                String telegramUsername) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.imgFile = imgFile;
        this.telegramUsername = telegramUsername;
        this.active = true;
        this.role = Role.USER;
    }

    public User(UserDto userDto, String filename) {
        this(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(),
                userDto.getAge().byteValue(), Sex.valueOf(userDto.getSex().toUpperCase()),
                userDto.getInfo(), filename, userDto.getTelegram());
    }

    public void update(UpdateUserDtoReq userDto) {
        this.username = userDto.getUsername();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.age = userDto.getAge();
        this.sex = Sex.valueOf(userDto.getSex());
        this.info = userDto.getInfo();
        this.imgFile = userDto.getFileName();
        this.telegramUsername = userDto.getTelegramUsername();
    }

    public void removeSupportAnswer(long id) {
        support.removeIf(elem -> elem.getId() == id);
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
