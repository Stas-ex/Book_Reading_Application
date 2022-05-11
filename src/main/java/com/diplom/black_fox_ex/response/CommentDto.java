package com.diplom.black_fox_ex.response;
import com.diplom.black_fox_ex.model.Comments;
import lombok.Data;

@Data
public class CommentDto {
    private long id;
    private String text;
    private String username;
    private String image;
    private String color;

    public CommentDto() {}

    public CommentDto(Comments comments) {
        this.id = comments.getId();
        this.username = comments.getUser().getUsername();
        this.text = comments.getText();
        this.image = comments.getUser().getImgFile();
        this.color = comments.getColor();
    }

    public CommentDto(Comments comments, String uploadPath) {
        this.id = comments.getId();
        this.username = comments.getUser().getUsername();
        this.text = comments.getText();
        this.image = uploadPath + comments.getUser().getImgFile();
        this.color = comments.getColor();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
