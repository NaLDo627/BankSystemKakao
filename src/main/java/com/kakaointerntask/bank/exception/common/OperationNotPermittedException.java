package com.kakaointerntask.bank.exception.common;

public class OperationNotPermittedException extends EntityNotFoundException {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }
}
