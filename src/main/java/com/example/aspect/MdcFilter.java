package com.example.aspect;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class MdcFilter extends HttpFilter{
	@Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, 
                            FilterChain chain) throws IOException, ServletException {
        try {
            String traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);

            String userId = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
            MDC.put("userId", userId);

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
