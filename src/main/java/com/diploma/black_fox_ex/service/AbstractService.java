package com.diploma.black_fox_ex.service;

import java.util.ArrayList;
import java.util.List;

public class AbstractService {

    public List<Integer> getPageNumbers(int numPage, int pageSize) {
        numPage = (numPage > 5) ? numPage : 2;

        List<Integer> pageNumbers = new ArrayList<>(List.of(1));
        for (int i = numPage; i < pageSize; i++) {
            pageNumbers.add(i);
        }

        return pageNumbers;
    }
}