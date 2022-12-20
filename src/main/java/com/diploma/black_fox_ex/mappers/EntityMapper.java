package com.diploma.black_fox_ex.mappers;


import com.diploma.black_fox_ex.dto.CommentDto;
import com.diploma.black_fox_ex.dto.book.BookReqDto;
import com.diploma.black_fox_ex.dto.user.UserDto;
import com.diploma.black_fox_ex.dto.user.UserMenuDto;
import com.diploma.black_fox_ex.dto.user.UserProfileDto;
import com.diploma.black_fox_ex.model.Book;
import com.diploma.black_fox_ex.model.Comment;
import com.diploma.black_fox_ex.model.User;
import com.diploma.black_fox_ex.model.constant.Sex;
import org.apache.commons.lang3.StringUtils;

import static com.diploma.black_fox_ex.io.ImgDirectories.USER_IMG_DIR;

public class EntityMapper {

    public static UserProfileDto toUserProfileDto(User user) {
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                StringUtils.EMPTY,
                user.getAge(),
                user.getSex().getTitle(),
                user.getInfo(),
                user.getTelegram(),
                null);
    }

    public static UserMenuDto toUserMenuDto(User user) {
        return new UserMenuDto(
                user.getUsername(),
                USER_IMG_DIR.getPath() + user.getImg(),
                user.getEmail()
        );
    }


    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getAge().byteValue(),
                Sex.valueOf(userDto.getSex().toUpperCase()),
                userDto.getInfo(),
                userDto.getFilename(),
                userDto.getTelegram());
    }

    public static Book toBook(User author,BookReqDto bookDto, String img) {
        return new Book(
                bookDto.getTitle(), img,
                bookDto.getBigText(),
                bookDto.getGenre(),
                author
        );
    }

    public static User updateUser(User user, UserProfileDto userDto) {
        user.setSex(Sex.valueOf(userDto.getSex().toUpperCase()));
        user.setAge(userDto.getAge().byteValue());
        user.setTelegram(userDto.getTelegram());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());
        user.setImg(userDto.getFilename());
        user.setEmail(userDto.getEmail());
        user.setInfo(userDto.getInfo());
        return user;
    }

    public static Book updateBook(Book book, BookReqDto bookDto, String img) {
        book.setTitle(bookDto.getTitle());
        book.setGenre(bookDto.getGenre());
        book.setBigText(bookDto.getBigText());
        book.setImg(img);
        return book;
    }

    public static Comment toComment(CommentDto commentDto, User user) {
        return new Comment(commentDto.getText(), commentDto.getColor(), user);
    }
}
