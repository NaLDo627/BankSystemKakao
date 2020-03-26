package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.EntityNotFoundException;
import lombok.Getter;

@Getter
public class TransactionHistoryNotFoundException extends EntityNotFoundException {
    GlobalErrorCode errorCode;

    public TransactionHistoryNotFoundException(Long historyNo, GlobalErrorCode errorCode) {
        super(String.format("TransactionHistory number %d : %s", historyNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
