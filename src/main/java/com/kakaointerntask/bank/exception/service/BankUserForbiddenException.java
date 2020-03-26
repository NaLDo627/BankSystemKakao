package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.Getter;

@Getter
public class BankUserForbiddenException extends OperationNotPermittedException {
    GlobalErrorCode errorCode;

    public BankUserForbiddenException(Integer userNo, GlobalErrorCode errorCode) {
        super(String.format("BankUser number %d : %s", userNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
