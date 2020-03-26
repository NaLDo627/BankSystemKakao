package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import lombok.Getter;

@Getter
public class BankUserBadRequestException extends OperationBadRequestException {
    GlobalErrorCode errorCode;

    public BankUserBadRequestException(Integer userNo, GlobalErrorCode errorCode) {
        super(String.format("BankUser number %d : %s", userNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
