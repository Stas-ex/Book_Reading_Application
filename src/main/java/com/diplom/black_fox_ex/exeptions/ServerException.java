package com.diplom.black_fox_ex.exeptions;

public class ServerException extends Throwable {
    private static AnswerErrorCode stringError;

    public ServerException(AnswerErrorCode stringError) {
        ServerException.stringError = stringError;
    }

    public String getErrorMessage() {
        return stringError.getMsg();
    }
}
