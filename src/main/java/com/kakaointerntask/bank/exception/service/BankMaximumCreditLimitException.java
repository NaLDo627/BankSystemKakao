package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;

public class BankMaximumCreditLimitException extends OperationBadRequestException {
    GlobalErrorCode errorCode;

    public BankMaximumCreditLimitException(Integer userNo, GlobalErrorCode errorCode) {
        super(String.format("BankUser number %d : %s", userNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
