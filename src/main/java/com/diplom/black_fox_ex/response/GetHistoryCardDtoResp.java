package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.io.FileDirectories;
import com.diplom.black_fox_ex.model.History;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class GetHistoryCardDtoResp {
    private long id;
    private String title;
    private String backgroundImg;
    private List<String> listAuthors = new ArrayList<>();
    private String smallText;
    private long countLike;
    private String tag;

    public GetHistoryCardDtoResp(History history) {
        this.id = history.getId();
        this.title = history.getTitle();
        this.backgroundImg = FileDirectories.HISTORY_IMG.getPath() + history.getBackgroundImg();
        this.countLike = history.getCountLike();
        this.tag = history.getTag().getName();
        history.getUsers().forEach(elem -> listAuthors.add(elem.getUsername()));
        String bigText = history.getBigText();

        if(bigText.length() <= 120)
            this.smallText = bigText.substring(0,history.getBigText().length() - 1);
        else
            this.smallText = bigText.substring(0,120) + "...";
    }
}



