package com.omarcosallan.saas_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundException extends SaaSException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("User not found.");
        problemDetail.setDetail("No users were found");

        return problemDetail;
    }
}
