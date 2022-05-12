package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.model.History;
import com.diplom.black_fox_ex.model.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class HistoryDto {
    private long id;
    private String title;
    private String backgroundImg;
    private String bigText;
    private List<String> listAuthors = new ArrayList<>();
    private List<CommentDto> listComment = new ArrayList<>();
    private String smallText;
    private long countLike;
    private String tag;


    public HistoryDto(History history) {
        this.id = history.getId();
        this.title = history.getTitle();
        this.backgroundImg = history.getBackgroundImg();
        this.bigText = history.getBigText().replaceAll("\\r","<br>").replaceAll("\\n","<br>");
        this.countLike = history.getCountLike();
        this.tag = history.getTags().iterator().next().getName();

        history.getUsers().forEach(elem -> listAuthors.add(elem.getUsername()));
        if(history.getBigText().length() <= 200){
            this.smallText = history.getBigText().substring(0,history.getBigText().length() - 1);
        }else {
            this.smallText = history.getBigText().substring(0,200) + "...";
        }
        List<CommentDto> list = new ArrayList<>();
        history.getComments().forEach(elem-> list.add(new CommentDto(elem)));

        this.listComment = list;
    }

    public HistoryDto(History history, String uploadPath) {
        this(history);

        this.id = history.getId();
        this.title = history.getTitle();
        this.backgroundImg = uploadPath + history.getBackgroundImg();
        this.bigText = history.getBigText().replaceAll("\\r","<br>").replaceAll("\\n","<br>");
        this.countLike = history.getCountLike();
        this.tag = history.getTags().iterator().next().getName();

        history.getUsers().forEach(elem -> listAuthors.add(elem.getUsername()));
        if(history.getBigText().length() <= 200){
            this.smallText = history.getBigText().substring(0,history.getBigText().length() - 1);
        }else {
            this.smallText = history.getBigText().substring(0,200) + "...";
        }
        List<CommentDto> list = new ArrayList<>();
        history.getComments().forEach(elem-> list.add(new CommentDto(elem,uploadPath)));

        this.listComment = list;
    }

}



