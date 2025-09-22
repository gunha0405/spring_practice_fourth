package com.example;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final MessageSource messageSource;

    public HelloController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/hello")
    public String hello() {
        // 현재 Locale에 맞는 메시지를 가져옴
        return messageSource.getMessage(
                "greeting.hello",
                null,
                LocaleContextHolder.getLocale()
        );
    }
}
