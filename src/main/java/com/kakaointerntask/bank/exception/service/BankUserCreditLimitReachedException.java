package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import com.kakaointerntask.bank.exception.controller.RequestForbiddenException;

public class BankUserCreditLimitReachedException extends BankUserBadRequestException {
    public BankUserCreditLimitReachedException(Integer userNo, GlobalErrorCode errorCode) {
        super(userNo, errorCode);
    }
}
