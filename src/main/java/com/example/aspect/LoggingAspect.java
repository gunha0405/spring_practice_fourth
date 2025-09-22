package com.example.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    // com.example.service 패키지의 모든 메서드
	@Before("execution(* com.example..service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("메서드 시작: {} - 파라미터: {}", 
                     joinPoint.getSignature(), 
                     joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.example..service..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("메서드 정상 종료: {} - 결과: {}", 
                     joinPoint.getSignature(), 
                     result);
    }

    @AfterThrowing(pointcut = "execution(* com.example..service..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("메서드 예외 발생: {} - 예외: {}", 
                      joinPoint.getSignature(), 
                      ex.getMessage(), ex);
    }
}
