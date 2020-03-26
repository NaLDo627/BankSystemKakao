package com.kakaointerntask.bank.exception.controller;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import lombok.Getter;

@Getter
public class RequestBadRequestException extends OperationBadRequestException {
    GlobalErrorCode errorCode;

    public RequestBadRequestException(GlobalErrorCode errorCode) {
        super("RequestBadRequestException : " + errorCode.getMessage());
        this.errorCode = errorCode;
    }
}