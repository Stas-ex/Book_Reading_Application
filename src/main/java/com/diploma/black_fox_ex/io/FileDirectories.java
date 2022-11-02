package com.diploma.black_fox_ex.io;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

public enum FileDirectories {

    USER_IMG("src/main/resources/img/user-img"),
    BOOK_IMG("src/main/resources/img/user-img"),
    IMG("src/main/resources/img");

    private String path;

    FileDirectories(String path) {
        setPath(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path.length() != 0) {
            try {
                this.path = new FileSystemResource(path).getURL().getPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
