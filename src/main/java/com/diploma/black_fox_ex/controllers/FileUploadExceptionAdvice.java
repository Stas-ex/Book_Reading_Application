package com.diploma.black_fox_ex.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * This class catches global server errors that the program did not handle.
 */
@ControllerAdvice
public class FileUploadExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadExceptionAdvice.class);

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(Exception.class)
    public ModelAndView handleMaxSizeException(HttpServletRequest req, Exception ex) {
        logger.error("URL: {} MSG: {}", req.getRequestURL(), ex.getMessage());
        Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).forEach(logger::error);
        ex.printStackTrace();

        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        return mav;
    }
}