package com.omarcosallan.saas_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TokenValidationException extends SaaSException {

    private String message;

    public TokenValidationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle(message);

        return problemDetail;
    }
}
