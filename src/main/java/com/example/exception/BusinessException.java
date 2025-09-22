package com.example.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String messageKey;

    public BusinessException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
