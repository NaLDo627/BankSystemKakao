package com.kakaointerntask.bank.exception.entity;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.BankbookForbiddenException;

public class BankbookTypeUnchangeableException extends BankUserEntityException {
    public BankbookTypeUnchangeableException(Integer bookNo, GlobalErrorCode errorCode) {
        super(bookNo, errorCode);
    }
}
