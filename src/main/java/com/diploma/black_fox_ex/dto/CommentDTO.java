package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.io.FileDirectories;
import com.diploma.black_fox_ex.model.Comment;
import lombok.Data;

@Data
public class CommentDTO {
    private String text;
    private String color;
    private String filename;
    private String username;

    public CommentDTO(Comment comment) {
        this.text = comment.getText();
        this.color = comment.getColor();
        this.filename = FileDirectories.USER_IMG_DIR.getPath() + comment.getUser().getImgFile();
        this.username = comment.getUser().getUsername();
    }
}
