package com.test.model;

import org.springframework.http.HttpStatus;

public class ProcessingException extends RuntimeException {
    public static final int PARSE_ERROR = -32700;
    public static final int METHOD_NOT_FOUND = -32601;
    public static final int INVALID_PARAMS = -32602;
    public static final int INTERNAL_ERROR = -32603;
    public static final int REQUEST_TIMED_OUT = -32000;

    private final int code;
    private final HttpStatus httpStatus;

    public ProcessingException(int code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ProcessingException(int code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
