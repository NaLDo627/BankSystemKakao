package com.kakaointerntask.bank.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kakaointerntask.bank.common.CommonResponse;
import com.kakaointerntask.bank.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {
    @Pointcut("execution(* com.kakaointerntask.bank.controller.entity.*.*(..))")
    public void entityControllerAspectTarget() { }

    @Around("entityControllerAspectTarget()")
    public Object logEntityController(ProceedingJoinPoint pjp) throws Throwable {
        String controllerName = pjp.getSignature().getDeclaringTypeName();
        String methodName = pjp.getSignature().getName();
        log.info(String.format("API requested in %s / %s ", controllerName, methodName));

        /* Log arguments */
        Object[] args = pjp.getArgs();
        log.info("arguments : ");
        for (Object arg : args) {
            log.info(arg.toString() + " :");
            try {
                log.info(JsonUtil.toJson(arg));
            } catch (JsonProcessingException e) {
                log.warn("Argument parsed failed ", e);
            }
        }

        Object result = pjp.proceed();

        if(!(result instanceof ResponseEntity<?>)) {
            return result;
        }

        Object response = ((ResponseEntity<?>)result).getBody();
        if(!(response instanceof CommonResponse)) {
            return result;
        }

        CommonResponse<?> commonResponse = (CommonResponse<?>)response;
        log.info(String.format("API response status : %d, message : %s",
                ((ResponseEntity<?>) result).getStatusCode().value(), commonResponse.getMessage()));
        return result;
    }

    @Pointcut("execution(* com.kakaointerntask.bank.controller.handler.*.*(..))")
    public void controllerExceptionAspectTarget() { }

    @Before("controllerExceptionAspectTarget()")
    public void logControllerException(JoinPoint joinPoint) {
        RuntimeException e = (RuntimeException)joinPoint.getArgs()[0];
        log.error("Exception occurred : ", e);
    }
}
