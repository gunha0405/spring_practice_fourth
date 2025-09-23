package com.example.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {
    private final String messageKey;

    public FileException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }
}
