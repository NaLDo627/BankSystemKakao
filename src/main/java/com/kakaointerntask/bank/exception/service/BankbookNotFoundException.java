package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.EntityNotFoundException;
import lombok.Getter;

@Getter
public class BankbookNotFoundException extends EntityNotFoundException {
    GlobalErrorCode errorCode;

    public BankbookNotFoundException(Integer bookNo, GlobalErrorCode errorCode) {
        super(String.format("Bankbook number %d : %s", bookNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }

    public BankbookNotFoundException(String bookId, GlobalErrorCode errorCode) {
        super(String.format("Bankbook id %s : %s", bookId, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
