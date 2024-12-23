package com.omarcosallan.saas_api.controllers;

import com.omarcosallan.saas_api.exceptions.SaaSException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SaaSException.class)
    public ProblemDetail handleSaaSException(SaaSException e) {
        return e.toProblemDetail();
    }
}
