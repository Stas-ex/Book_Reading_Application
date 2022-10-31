package com.diploma.black_fox_ex.model.constant;

public enum Genre {
    DOCUMENTARY,
    EDUCATIONAL,
    PSYCHOLOGY,
    ADVENTURES,
    SCIENTIFIC,
    REFERENCE,
    DETECTIVE,
    RELIGIOUS,
    TECHNIQUE,
    CHILDREN,
    BUSINESS,
    FOLKLORE,
    FANTASY,
    POETRY,
    NOVEL,
    HUMOR,
    HOBBY;

    public String getTitle() {
        return this.name().toLowerCase();
    }

}