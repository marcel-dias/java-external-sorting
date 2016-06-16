package com.marceldias.externalsorting.exception;

public class ExternalSortingException extends RuntimeException {

    public ExternalSortingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalSortingException(String message) {
        super(message);
    }
}
