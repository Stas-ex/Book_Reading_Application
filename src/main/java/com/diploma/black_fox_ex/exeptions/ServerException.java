package com.diploma.black_fox_ex.exeptions;

/**
 *This class takes an error class and returns its message.
 */
public class ServerException extends Throwable {
    private static AnswerErrorCode stringError;

    public ServerException(AnswerErrorCode stringError) {
        ServerException.stringError = stringError;
    }

    /**
     * @return an error message
     */
    public String getErrorMessage() {
        return stringError.getMsg();
    }
}
