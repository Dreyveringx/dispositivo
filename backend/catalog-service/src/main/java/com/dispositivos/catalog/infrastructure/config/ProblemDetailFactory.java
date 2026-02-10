package com.dispositivos.catalog.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.Map;


public final class ProblemDetailFactory {

    private static final String TITLE_NOT_FOUND = "Resource not found";
    private static final String TITLE_VALIDATION = "Validation failed";
    private static final String DETAIL_VALIDATION = "One or more fields are invalid";
    private static final String TITLE_INTERNAL = "Internal server error";
    private static final String DETAIL_INTERNAL = "An unexpected error occurred";

    private ProblemDetailFactory() {
    }

    public static ProblemDetail createNotFound(String detail, String instanceUri) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail);
        problem.setType(ProblemTypes.NOT_FOUND);
        problem.setTitle(TITLE_NOT_FOUND);
        problem.setInstance(URI.create(instanceUri));
        return problem;
    }

    public static ProblemDetail createValidation(Map<String, String> errors, String instanceUri) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, DETAIL_VALIDATION);
        problem.setType(ProblemTypes.VALIDATION_ERROR);
        problem.setTitle(TITLE_VALIDATION);
        problem.setProperty("errors", errors);
        problem.setInstance(URI.create(instanceUri));
        return problem;
    }

    public static ProblemDetail createInternal(String instanceUri) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, DETAIL_INTERNAL);
        problem.setType(ProblemTypes.INTERNAL_ERROR);
        problem.setTitle(TITLE_INTERNAL);
        problem.setInstance(URI.create(instanceUri));
        return problem;
    }
}
