package com.sns.elasticsearch.global.exception;

public class IndexingException extends RuntimeException {
    public IndexingException(String message) {
        super(message);
    }

    public IndexingException(String message, Throwable cause) {
        super(message, cause);
    }
}
