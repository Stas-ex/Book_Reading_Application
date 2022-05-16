package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.model.Comment;
import lombok.Data;

@Data
public class GetCommentsDtoResp {
    private String text;
    private String color;
    private String fileName;
    private String username;

    public GetCommentsDtoResp(Comment comment) {
        this.text = comment.getText();
        this.color = comment.getColor();
        this.fileName = FileDirectories.USER_IMG.getPath() + comment.getUser().getImgFile();
        this.username = comment.getUser().getUsername();
    }
}
