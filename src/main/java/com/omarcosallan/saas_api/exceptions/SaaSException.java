package com.omarcosallan.saas_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class SaaSException extends RuntimeException {

    public SaaSException() {}

    public SaaSException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pb.setTitle("Fleetwise internal server error");
        return pb;
    }
}
