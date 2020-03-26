package com.kakaointerntask.bank.controller.transaction;

import com.kakaointerntask.bank.annotation.TransactionPermissionCheck;
import com.kakaointerntask.bank.common.CommonResponse;
import com.kakaointerntask.bank.annotation.ValidationCheck;
import com.kakaointerntask.bank.dto.TransactionRequestDTO;
import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.controller.RequestForbiddenException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionUnavailableException;
import com.kakaointerntask.bank.service.BankbookService;
import com.kakaointerntask.bank.service.implement.TransactionDepositService;
import com.kakaointerntask.bank.service.implement.TransactionTransferService;
import com.kakaointerntask.bank.service.implement.TransactionWithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionDepositService transactionDepositService;
    private final TransactionWithdrawService transactionWithdrawService;
    private final TransactionTransferService transactionTransferService;

    @ValidationCheck
    @TransactionPermissionCheck(requestUserNo = "#transactionRequestDTO.getRequestUserNo()", requestBookId = "#transactionRequestDTO.getTransactionBookId()")
    @RequestMapping(value = "/deposit", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionDeposit(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
                                                                       Errors errors, HttpSession session) {

        try {
            TransactionResultDTO resultDTO = transactionDepositService.handleTransactionRequest(transactionRequestDTO);
            return ResponseEntity.ok(CommonResponse.getResponseWithData(GlobalErrorCode.NO_ERROR.getMessage(), GlobalErrorCode.NO_ERROR.getErrorCode(), resultDTO));
        } catch (OptimisticLockingFailureException e) {
            throw new TransactionUnavailableException(null, GlobalErrorCode.TRANSACTION_UNAVAILABLE);
        }
    }

    @ValidationCheck
    @TransactionPermissionCheck(requestUserNo = "#transactionRequestDTO.getRequestUserNo()", requestBookId = "#transactionRequestDTO.getTransactionBookId()")
    @RequestMapping(value = "/withdraw", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionWithdraw(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
                                                                                  Errors errors, HttpSession session) {

        try {
            TransactionResultDTO resultDTO = transactionWithdrawService.handleTransactionRequest(transactionRequestDTO);
            return ResponseEntity.ok(CommonResponse.getResponseWithData(GlobalErrorCode.NO_ERROR.getMessage(), GlobalErrorCode.NO_ERROR.getErrorCode(), resultDTO));
        } catch (OptimisticLockingFailureException e) {
            throw new TransactionUnavailableException(null, GlobalErrorCode.TRANSACTION_UNAVAILABLE);
        }
    }

    @ValidationCheck
    @TransactionPermissionCheck(requestUserNo = "#transactionRequestDTO.getRequestUserNo()", requestBookId = "#transactionRequestDTO.getTransactionBookId()")
    @RequestMapping(value = "/transfer", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    ResponseEntity<CommonResponse<TransactionResultDTO>> handleTransactionTransfer(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO,
                                                                                  Errors errors, HttpSession session) {

        try {
            TransactionResultDTO resultDTO = transactionTransferService.handleTransactionRequest(transactionRequestDTO);
            return ResponseEntity.ok(CommonResponse.getResponseWithData(GlobalErrorCode.NO_ERROR.getMessage(), GlobalErrorCode.NO_ERROR.getErrorCode(), resultDTO));
        } catch (OptimisticLockingFailureException e) {
            throw new TransactionUnavailableException(null, GlobalErrorCode.TRANSACTION_UNAVAILABLE);
        }
    }
}
