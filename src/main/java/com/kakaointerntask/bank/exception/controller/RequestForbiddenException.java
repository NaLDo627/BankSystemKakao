package com.kakaointerntask.bank.exception.controller;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotFoundException;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.Getter;

@Getter
public class RequestForbiddenException extends OperationNotPermittedException {
    GlobalErrorCode errorCode;

    public RequestForbiddenException(GlobalErrorCode errorCode) {
        super("RequestForbiddenException : " + errorCode.getMessage());
        this.errorCode = errorCode;
    }
}