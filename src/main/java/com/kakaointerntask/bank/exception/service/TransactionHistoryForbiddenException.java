package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.Getter;

@Getter
public class TransactionHistoryForbiddenException extends OperationNotPermittedException {
    GlobalErrorCode errorCode;

    public TransactionHistoryForbiddenException(Long historyNo, GlobalErrorCode errorCode) {
        super(String.format("TransactionHistory number %d : %s", historyNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
