package com.diploma.black_fox_ex.io;

public enum FileDirectories {
    USER_IMG("/Users/stas_ex/Diploma_Spring/src/main/resources/img/user-img/"),
    BOOK_IMG("/Users/stas_ex/Diploma_Spring/src/main/resources/img/book-img/");

    private String path;

    FileDirectories(String msg) {
        setPath(msg);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path.length() != 0) {
            this.path = path;
        }
    }
}
