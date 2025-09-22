package com.example.exception;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // 공통: 메시지 가져오기 (없으면 기본 메시지 사용)
    private String getMessage(String key, String defaultMsg, Locale locale) {
        return messageSource.getMessage(key, null, defaultMsg, locale);
    }

    // 커스텀 예외 처리
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, Locale locale, Model model) {
        String msg = getMessage(ex.getMessageKey(), ex.getMessage(), locale);
        model.addAttribute("error", new ErrorView(HttpStatus.BAD_REQUEST.value(), msg, LocalDateTime.now()));
        return "error";
    }

    // 데이터 없음 예외 처리
    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFound(DataNotFoundException ex, Locale locale, Model model) {
        String msg = getMessage("error.data.notFound", "Data not found", locale);
        model.addAttribute("error", new ErrorView(HttpStatus.NOT_FOUND.value(), msg, LocalDateTime.now()));
        return "error";
    }

    // 기타 예상 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Locale locale, Model model) {
        String msg = getMessage("error.internal", "Internal server error", locale);
        model.addAttribute("error", new ErrorView(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, LocalDateTime.now()));
        return "error";
    }
}
