package com.kakaointerntask.bank.exception.entity;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.BankbookBadRequestException;
import com.kakaointerntask.bank.exception.service.BankbookForbiddenException;

public class BankbookMaximumBalanceLimitException extends BankbookEntityException {
    public BankbookMaximumBalanceLimitException(Integer bookNo, GlobalErrorCode errorCode) {
        super(bookNo, errorCode);
    }
}
