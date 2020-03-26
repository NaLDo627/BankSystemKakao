package com.kakaointerntask.bank.exception.service;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.EntityNotFoundException;
import lombok.Getter;

@Getter
public class BankUserNotFoundException extends EntityNotFoundException {
    GlobalErrorCode errorCode;

    public BankUserNotFoundException(Integer userNo, GlobalErrorCode errorCode) {
        super(String.format("BankUser number %d : %s", userNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }

    public BankUserNotFoundException(String userId, GlobalErrorCode errorCode) {
        super(String.format("BankUser id %s : %s", userId, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
