package com.kakaointerntask.bank.exception.service.transaction;

import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import lombok.Getter;

@Getter
public class TransactionUnavailableException extends TransactionException {
    public TransactionUnavailableException(TransactionResultDTO resultDTO, GlobalErrorCode errorCode) {
        super(resultDTO, errorCode);
    }
}
