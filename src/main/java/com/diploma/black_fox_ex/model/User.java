package com.diploma.black_fox_ex.model;

import com.diploma.black_fox_ex.dto.user.UserDto;
import com.diploma.black_fox_ex.model.constant.Role;
import com.diploma.black_fox_ex.model.constant.Sex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "active")
    private Boolean active;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private Byte age;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "info", length = 1000)
    private String info;

    @Column(name = "filename")
    private String img;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "date_lock")
    private LocalDateTime dateTimeDeletion;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_book_favorite",
            joinColumns = @JoinColumn(name = "fk_users_id"),
            inverseJoinColumns = @JoinColumn(name = "fk_book_id"))
    private List<Book> favorite = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_support", joinColumns =
    @JoinColumn(name = "fk_support_id"), inverseJoinColumns =
    @JoinColumn(name = "fk_user_id"))
    private List<Support> support = new ArrayList<>();

    public User(String username,
                String email,
                String password,
                Byte age,
                Sex sex,
                String info,
                String img,
                String telegram) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.info = info;
        this.img = img;
        this.telegram = telegram;
        this.active = true;
        this.role = Role.USER;
    }

    public void update(UserDto userDto) {
        this.username = userDto.getUsername();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.age = userDto.getAge().byteValue();
        this.sex = Sex.valueOf(userDto.getSex().toUpperCase());
        this.info = userDto.getInfo();
        this.img = userDto.getFilename();
        this.telegram = userDto.getTelegram();
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
        return active;
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

    public void addFavorite(Book book){
        this.favorite.add(book);
    }

    public void removeFavorite(Book book){
        this.favorite.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
