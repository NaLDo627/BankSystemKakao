package com.kakaointerntask.bank.exception.entity;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.BankUserForbiddenException;
import com.kakaointerntask.bank.exception.service.BankbookBadRequestException;

public class BankUserUpdateForbiddenException extends BankUserEntityException {
    public BankUserUpdateForbiddenException(Integer userNo, GlobalErrorCode errorCode) {
        super(userNo, errorCode);
    }
}
