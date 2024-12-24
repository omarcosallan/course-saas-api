package com.omarcosallan.saas_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class MemberNotFoundException extends SaaSException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Member not found.");
        problemDetail.setDetail("No members were found");

        return problemDetail;
    }
}
