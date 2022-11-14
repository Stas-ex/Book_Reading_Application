package com.diploma.black_fox_ex.dto;

import java.util.List;

public record PageSplitView<T>(T elem, List<Integer> pageNumbers) { }