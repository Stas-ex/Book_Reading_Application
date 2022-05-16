package com.diplom.black_fox_ex.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;


@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(Exception.class)//MaxUploadSizeExceededException
    public String handleMaxSizeException(Model model, Exception exc) {
        model.addAttribute("error", Objects.requireNonNull(exc.getMessage()).split(";")[0]);
        return "/errorPage";
    }
}