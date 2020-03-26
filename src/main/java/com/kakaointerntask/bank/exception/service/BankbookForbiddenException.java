package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.Getter;

@Getter
public class  BankbookForbiddenException extends OperationNotPermittedException {
    GlobalErrorCode errorCode;

    public BankbookForbiddenException(Integer bookNo, GlobalErrorCode errorCode) {
        super(String.format("Bankbook number %d : %s", bookNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
