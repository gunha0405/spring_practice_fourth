package com.example.config;

import java.io.IOException;



import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private final MessageSource messageSource;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage;

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = messageSource.getMessage("error.user.notFound", null, request.getLocale());
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = messageSource.getMessage("error.login.badCredentials", null, request.getLocale());
        } else {
            errorMessage = messageSource.getMessage("error.login.failed", null, request.getLocale());
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/user/login?error");
    }
}

