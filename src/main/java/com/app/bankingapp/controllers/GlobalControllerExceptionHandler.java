package com.app.bankingapp.controllers;

import com.app.bankingapp.exceptions.ApplicationException;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ErrorInfo handleNotFound(Exception exception) {
        return new ErrorInfo(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public ErrorInfo handleBadRequest(Exception exception) {
        return new ErrorInfo(exception.getMessage());
    }
}
