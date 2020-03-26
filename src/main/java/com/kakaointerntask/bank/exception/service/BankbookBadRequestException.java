package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.EntityNotFoundException;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import lombok.Getter;

@Getter
public class BankbookBadRequestException extends OperationBadRequestException {
    GlobalErrorCode errorCode;

    public BankbookBadRequestException(Integer bookNo, GlobalErrorCode errorCode) {
        super(String.format("Bankbook number %d : %s", bookNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
