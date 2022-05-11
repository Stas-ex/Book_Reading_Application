package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResponseAllHistory {
    Map<Integer,List<HistoryDto>> map = new TreeMap<>();
    String error;
    List<String> tags = new ArrayList<>();

    public Map<Integer, List<HistoryDto>> getMap() {
        return map;
    }

    public void setMap(Map<Integer, List<HistoryDto>> map) {
        this.map = map;
    }

    public String getError() {
        return error;
    }

    public List<Integer> getAllBtnNum() {
        List<Integer> list = new ArrayList<>();
        if(map != null) {
            for (int counter = 0; counter < map.size(); counter++) {
                list.add(counter);
            }
        }
        return list;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
         tags.forEach(elem-> this.tags.add(elem.getName()));
    }
}
