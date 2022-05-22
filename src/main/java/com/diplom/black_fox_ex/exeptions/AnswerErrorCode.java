package com.diplom.black_fox_ex.exeptions;

public enum AnswerErrorCode {
    REGISTRATION_WRONG_USERNAME("Username is too short"),
    REGISTRATION_WRONG_AGE_SYNTAX("Syntax error when entering age"),
    REGISTRATION_WRONG_AGE_RANGE("Invalid age range"),
    REGISTRATION_WRONG_VALIDATE_EMAIL("Invalid email"),
    REGISTRATION_WRONG_VALIDATE_EMAIL_ALREADY_EXIST("This Email already exist"),
    REGISTRATION_WRONG_VALIDATE_PASSWORD("Password error"),
    REGISTRATION_WRONG_VALIDATE_IMG("Such a picture does not exist"),
    REGISTRATION_WRONG_VALIDATE_INFO("Have written too little about yourself"),
    REGISTRATION_WRONG_VALIDATE_SEX("Incorrect gender information"),
    REGISTRATION_USERNAME_ALREADY_EXIST("This username already exist"),

    UPDATE_WRONG_USERNAME("Username is too short"),
    UPDATE_WRONG_AGE("Incorrect age"),
    UPDATE_WRONG_VALIDATE_EMAIL("Invalid email"),
    UPDATE_NOT_ROOT_UPDATE("The user does not have permission to update"),
    UPDATE_WRONG_VALIDATE_EMAIL_ALREADY_EXIST("This Email already exist"),
    UPDATE_WRONG_VALIDATE_PASSWORD("Password error"),
    UPDATE_WRONG_VALIDATE_IMG("Such a picture does not exist"),
    UPDATE_WRONG_VALIDATE_INFO("Have written too little about yourself"),
    UPDATE_WRONG_VALIDATE_SEX("Specify your age"),
    UPDATE_USERNAME_ALREADY_EXIST("This username already exist"),
    UPDATE_WRONG_TELEGRAM_ADDRESS("Error entering username for telegram"),
    UPDATE_WRONG_AGE_SYNTAX("Syntax error when entering age"),
    UPDATE_WRONG_AGE_RANGE("Invalid age range"),

    USER_NOT_REGISTERED("User is not registered"),

    FILE_CREATE_ERROR("Failed to download file"),

    HISTORY_ID_NOT_EXIST("There is no history with such an id!"),
    HISTORY_SHORT_TEXT("Too short story text!"),
    HISTORY_IMG_ERROR("Failed to upload picture!"),
    HISTORY_TAG_ERROR("Error you did not specify a tag!"),
    HISTORY_TITLE_ERROR("The title of the story is too short!"),
    HISTORY_USER_NOT_FOUND("User does not exist!"),
    HISTORY_NOT_FOUND("History does not exist!"),
    HISTORY_PAGE_ERROR("There is no such page"),
    PAGE_IS_EMPTY("Blank page"),

    COMMENT_BIG_TEXT_ERROR("Your comment is too small"),
    COMMENT_COLOR_ERROR("Error color!"),

    FAVORITE_USER_ERROR("User does not exist"),
    FAVORITE_HISTORY_ID_ERROR("History id error"),
    ERROR_ANSWER_BY_USER("Sorry, you have no support answers");


    private String msg;
    AnswerErrorCode(String msg) {
        setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        if(msg.length() != 0) {
            this.msg = msg;
        }
    }
}
