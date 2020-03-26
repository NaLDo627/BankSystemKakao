package com.kakaointerntask.bank.controller.handler;

import com.kakaointerntask.bank.common.ExceptionUtil;
import com.kakaointerntask.bank.exception.common.EntityNotFoundException;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import com.kakaointerntask.bank.exception.common.OperationNotFoundException;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice(annotations = Controller.class, basePackages = "com.kakaointerntask.bank.controller.view")
public class ViewControllerExceptionHandler {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            OperationBadRequestException.class,
            MethodArgumentTypeMismatchException.class
    })
    public String handleBadRequestException(OperationBadRequestException e) {
        return "error/400";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = OperationNotPermittedException.class)
    public String handleNotPermittedException(OperationNotPermittedException e) {
        return "error/403";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = { EntityNotFoundException.class, OperationNotFoundException.class })
    public String handleNotFoundException(RuntimeException e) {
        return "error/404";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String handleUnknownException(Exception e) {
        return "error/500";
    }
}
