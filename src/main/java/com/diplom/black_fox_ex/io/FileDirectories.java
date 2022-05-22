package com.diplom.black_fox_ex.io;

public enum FileDirectories {
    USER_IMG("/Users/stas_ex/IdeaProjects/Diploma_Spring/src/main/resources/img/user-img/"),
    HISTORY_IMG("/Users/stas_ex/IdeaProjects/Diploma_Spring/src/main/resources/img/history-img/");

    private String path;
    FileDirectories(String msg) {
        setPath(msg);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String msg) {
        if(msg.length() != 0) {
            this.path = msg;
        }
    }
}
