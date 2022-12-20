package com.diploma.black_fox_ex.io;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

public enum ImgDirectories {

    IMG("src/main/resources/img/"),
    USER_IMG_DIR("src/main/resources/img/user-img/"),
    BOOK_IMG_DIR("src/main/resources/img/book-img/");

    private String path;

    ImgDirectories(String path) {
        setPath(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (!path.isBlank()) {
            try {
                this.path = new FileSystemResource(path).getURL().getPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
