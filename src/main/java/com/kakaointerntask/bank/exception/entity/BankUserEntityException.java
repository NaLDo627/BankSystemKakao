package com.kakaointerntask.bank.exception.entity;

import com.kakaointerntask.bank.exception.GlobalErrorCode;
import lombok.Getter;

@Getter
public class BankUserEntityException extends RuntimeException {
    GlobalErrorCode errorCode;

    public BankUserEntityException(Integer userNo, GlobalErrorCode errorCode) {
        super(String.format("BankUser number %d : %s", userNo, errorCode.getMessage()));
        this.errorCode = errorCode;
    }
}
