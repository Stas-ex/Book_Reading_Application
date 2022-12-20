package com.diploma.black_fox_ex.dto;

import com.diploma.black_fox_ex.model.Comment;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDto {

    @NotBlank(message = "exception.comment.text.blank")
    @Size(min = 5, message = "exception.comment.text.small")
    private String text;

    @NotBlank(message = "exception.comment.color.blank")
    private String color;

    public CommentDto(Comment comment) {
        this.text = comment.getText();
        this.color = comment.getColor();
    }
}
