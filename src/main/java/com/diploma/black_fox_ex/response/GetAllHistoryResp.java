package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.model.History;
import com.diploma.black_fox_ex.model.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllHistoryResp {
    private List<GetHistoryCardDtoResp> listDto = new ArrayList<>();
    private String error;
    private List<String> tags = new ArrayList<>();
    private List<Integer> pageNumbers;


    public void setTags(List<Tag> tags) {
        tags.forEach(tag -> this.tags.add(tag.getName()));
    }

    public void setList(List<History> list) {
        list.forEach(history -> this.listDto.add(new GetHistoryCardDtoResp(history)));
    }
}
