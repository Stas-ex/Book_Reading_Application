package com.diploma.black_fox_ex.model.constant;

public enum Sex {

    MAN, WOMAN;

    public String getTitle() {
        return this.name().toLowerCase();
    }
}
