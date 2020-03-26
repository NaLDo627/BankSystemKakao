package com.kakaointerntask.bank.exception.entity;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.BankbookBadRequestException;
import lombok.Getter;

@Getter
public class BankbookEntityException extends RuntimeException {
    GlobalErrorCode errorCode;

    public BankbookEntityException(Integer bookNo, GlobalErrorCode errorCode) {
        super(String.format("Bankbook number %d : %s", bookNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
