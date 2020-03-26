package com.kakaointerntask.bank.exception.common;

public class OperationNotFoundException extends EntityNotFoundException {
    public OperationNotFoundException(String msg) {
        super(msg);
    }
}
