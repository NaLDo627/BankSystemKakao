package com.kakaointerntask.bank.aspect;

import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import com.kakaointerntask.bank.exception.controller.RequestBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Aspect
@Component
@Slf4j
public class ControllerValidationAspect {
    @Pointcut("@annotation(com.kakaointerntask.bank.annotation.ValidationCheck)")
    public void checkValidationAspectTarget() { }

    @Before("checkValidationAspectTarget()")
    public void checkValidation(JoinPoint joinPoint) {
        Errors errors = null;

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Errors)
                errors = (Errors) arg;
        }

        if (errors == null)
            return;

        if (errors.hasErrors())
            throw new OperationBadRequestException(errors.getAllErrors().get(0).getDefaultMessage());
    }
}
