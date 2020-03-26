package com.kakaointerntask.bank.exception.service.transaction;

import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import lombok.Getter;

@Getter
public class TransactionForbiddenException extends TransactionException {
    public TransactionForbiddenException(TransactionResultDTO resultDTO, GlobalErrorCode errorCode) {
        super(resultDTO, errorCode);
    }
}
