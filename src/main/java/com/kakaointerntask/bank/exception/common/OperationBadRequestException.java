package com.kakaointerntask.bank.exception.common;

public class OperationBadRequestException extends EntityNotFoundException {
    public OperationBadRequestException(String msg) {
        super(msg);
    }
}
