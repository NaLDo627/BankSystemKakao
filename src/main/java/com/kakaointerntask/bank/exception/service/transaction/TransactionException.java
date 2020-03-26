package com.kakaointerntask.bank.exception.service.transaction;

import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    TransactionResultDTO result;
    GlobalErrorCode errorCode;

    public TransactionException(TransactionResultDTO resultDTO, GlobalErrorCode errorCode) {
        super(errorCode.getMessage());
        this.result = resultDTO;
        this.errorCode = errorCode;
    }
}
