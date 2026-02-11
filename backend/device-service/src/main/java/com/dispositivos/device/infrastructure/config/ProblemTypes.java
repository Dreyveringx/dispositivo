package com.dispositivos.device.infrastructure.config;

import java.net.URI;

public final class ProblemTypes {

    private ProblemTypes() {
    }

    public static final URI NOT_FOUND = URI.create("urn:problem-type:not-found");

    public static final URI VALIDATION_ERROR = URI.create("urn:problem-type:validation-error");

    public static final URI BAD_REQUEST = URI.create("urn:problem-type:bad-request");

    public static final URI CONFLICT = URI.create("urn:problem-type:conflict");

    public static final URI INTERNAL_ERROR = URI.create("urn:problem-type:internal-error");
}
