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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    public List<String> getListAuthors() {
        return listAuthors;
    }

    public void setListAuthors(List<String> listAuthors) {
        this.listAuthors = listAuthors;
    }

    public String getSmallText() {
        return smallText;
    }

    public void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    public List<CommentDto> getListComment() {
        return listComment;
    }

    public void setListComment(List<CommentDto> listComment) {
        this.listComment = listComment;
    }

    public long getCountLike() {
        return countLike;
    }

    public void setCountLike(long countLike) {
        this.countLike = countLike;
    }

    public String getTag() {
        if(tag == null){
            tag = "History";
        }
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}



