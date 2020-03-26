package com.kakaointerntask.bank.exception.controller;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotFoundException;
import lombok.Getter;

@Getter
public class RequestNotFoundException extends OperationNotFoundException {
    GlobalErrorCode errorCode;

    public RequestNotFoundException(GlobalErrorCode errorCode) {
        super("RequestNotFoundException : " + errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
