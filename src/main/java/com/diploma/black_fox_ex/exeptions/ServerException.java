package com.diploma.black_fox_ex.exeptions;

/**
 *This class takes an error class and returns its message.
 */
@Deprecated
public class ServerException extends Throwable {

    private static AnswerErrorCode stringError;

    public ServerException(AnswerErrorCode stringError) {
        ServerException.stringError = stringError;
    }

    public String getErrorMessage() {
        return stringError.getMsg();
    }
}
