package com.diplom.black_fox_ex.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

/**
 *
 * This class catches global server errors that the program did not handle.
 */
@ControllerAdvice
public class FileUploadExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadExceptionAdvice.class);

    /**
     *
     * @param model for creating attributes sent to the server as a response
     * @param ex contains a caught server error
     * @return the error page
     */
    @ExceptionHandler(Exception.class)//MaxUploadSizeExceededException
    public String handleMaxSizeException(Model model, Exception ex) {
        logger.error("Error handleMaxSizeException -> {}", ex.getMessage());
        model.addAttribute("error", Objects.requireNonNull(ex.getMessage()).split(";")[0]);
        return "/errorPage";
    }
}