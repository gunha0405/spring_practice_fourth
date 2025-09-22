package com.example.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorView {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
