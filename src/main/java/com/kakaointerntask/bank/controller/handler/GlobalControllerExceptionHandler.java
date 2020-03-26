package com.kakaointerntask.bank.controller.handler;

import com.kakaointerntask.bank.common.CommonResponse;
import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.exception.common.EntityNotFoundException;
import com.kakaointerntask.bank.exception.common.OperationBadRequestException;
import com.kakaointerntask.bank.exception.common.OperationNotFoundException;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import com.kakaointerntask.bank.exception.controller.RequestBadRequestException;
import com.kakaointerntask.bank.exception.controller.RequestForbiddenException;
import com.kakaointerntask.bank.exception.controller.RequestNotFoundException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionBadRequestException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionForbiddenException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleUnknownException(Exception e) {
        return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { OperationBadRequestException.class })
    public ResponseEntity<CommonResponse<Void>> handleBadRequestException(OperationBadRequestException e) {
        return handleException(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = OperationNotPermittedException.class)
    public ResponseEntity<CommonResponse<Void>> handleNotPermittedException(OperationNotPermittedException e) {
        return handleException(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { OperationNotFoundException.class, EntityNotFoundException.class })
    public ResponseEntity<CommonResponse<Void>> handleNotFoundException(RuntimeException e) {
        return handleException(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { RequestBadRequestException.class })
    public ResponseEntity<CommonResponse<Void>> handleRequestBadRequestException(RequestBadRequestException e) {
        return handleException(e, HttpStatus.BAD_REQUEST, e.getErrorCode().getErrorCode().intValue());
    }

    @ExceptionHandler(value = { RequestForbiddenException.class })
    public ResponseEntity<CommonResponse<Void>> handleRequestForbiddenException(RequestForbiddenException e) {
        return handleException(e, HttpStatus.FORBIDDEN, e.getErrorCode().getErrorCode().intValue());
    }

    @ExceptionHandler(value = { RequestNotFoundException.class })
    public ResponseEntity<CommonResponse<Void>> handleRequestNotFoundException(RequestNotFoundException e) {
        return handleException(e, HttpStatus.NOT_FOUND, e.getErrorCode().getErrorCode().intValue());
    }

    @ExceptionHandler(value = { TransactionUnavailableException.class })
    public ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionUnavailableException(TransactionUnavailableException e) {
        return handleException(e, HttpStatus.SERVICE_UNAVAILABLE, e.getResult(), e.getErrorCode().getErrorCode());
    }

    @ExceptionHandler(value = { TransactionBadRequestException.class })
    public ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionBadRequestException(TransactionBadRequestException e) {
        return handleException(e, HttpStatus.BAD_REQUEST, e.getResult(), e.getErrorCode().getErrorCode());
    }

    @ExceptionHandler(value = { TransactionForbiddenException.class })
    public ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionForbiddenException(TransactionForbiddenException e) {
        return handleException(e, HttpStatus.FORBIDDEN, e.getResult(), e.getErrorCode().getErrorCode());
    }

    @ExceptionHandler(value = { TransactionException.class })
    public ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionUnknownErrorException(TransactionForbiddenException e) {
        return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getResult(), e.getErrorCode().getErrorCode());
    }

    private ResponseEntity<CommonResponse<Void>> handleException(Throwable e, HttpStatus status) {
        return handleException(e, status, status.value());
    }

    private ResponseEntity<CommonResponse<Void>> handleException(Throwable e, HttpStatus status, int errorCode) {
        return ResponseEntity.status(status)
                .body(CommonResponse.getResponse(e.getMessage(), errorCode));
    }

    private <T> ResponseEntity<CommonResponse<T>> handleException(Throwable e, HttpStatus status, T data) {
        return handleException(e, status, data, status.value());
    }

    private <T> ResponseEntity<CommonResponse<T>> handleException(Throwable e, HttpStatus status, T data, int errorCode) {
        return ResponseEntity.status(status)
                .body(CommonResponse.getResponseWithData(e.getMessage(), errorCode, data));
    }
}
