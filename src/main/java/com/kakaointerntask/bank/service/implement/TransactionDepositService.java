package com.kakaointerntask.bank.service.implement;

import com.kakaointerntask.bank.dto.TransactionRequestDTO;
import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import com.kakaointerntask.bank.exception.entity.BankbookEntityException;
import com.kakaointerntask.bank.exception.entity.BankbookMaximumBalanceLimitException;
import com.kakaointerntask.bank.exception.service.*;
import com.kakaointerntask.bank.exception.service.transaction.TransactionBadRequestException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionException;
import com.kakaointerntask.bank.exception.service.transaction.TransactionUnavailableException;
import com.kakaointerntask.bank.service.BankUserService;
import com.kakaointerntask.bank.service.BankbookService;
import com.kakaointerntask.bank.service.TransactionHistoryService;
import com.kakaointerntask.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.validation.Valid;

@Slf4j
@Service("transactionDepositService")
@RequiredArgsConstructor
public class TransactionDepositService implements TransactionService {
    private final BankUserService bankUserService;
    private final BankbookService bankbookService;
    private final TransactionHistoryService transactionHistoryService;

    // @TODO 요청 검증 로직 모듈화
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransactionResultDTO handleTransactionRequest(@Valid TransactionRequestDTO requestDTO) {
        log.info(("current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName()));
        Bankbook bankbook = bankbookService.findBankbookByBankbookId(requestDTO.getTransactionBookId());
        TransactionResultDTO resultDTO = handleTransactionDeposit(requestDTO, bankbook);
        transactionHistoryService.saveTransactionHistory(resultDTO.toHistoryEntity());
        return resultDTO;
    }

    private TransactionResultDTO handleTransactionDeposit(TransactionRequestDTO requestDTO, Bankbook bankbook) {
        TransactionResultDTO.TransactionResultDTOBuilder resultDTOBuilder = TransactionResultDTO.builder()
                .requestUserNo(requestDTO.getRequestUserNo())
                .requestBankbookAlias(bankbook.getAlias())
                .requestBankbookId(bankbook.getBankbookId())
                .requestBankbookPreviousBalance(bankbook.getBalance())
                .requestAmount(requestDTO.getAmount())
                .type(TransactionType.DEPOSIT)
                .memo(requestDTO.getMemo());

        int depositAmount = requestDTO.getAmount();

        // @TODO 수수료 로직 모듈화
        resultDTOBuilder.transactionFee(0);
        resultDTOBuilder.actualAmount(depositAmount);

        if (depositAmount > 500000000) {
            resultDTOBuilder.requestBankbookRemainBalance(bankbook.getBalance());
            throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.MAXIMUM_TRANSACTION_AMOUNT_REACHED);
        }

        try {
            bankbookService.addBalanceToBankbook(bankbook.getBookNo(), depositAmount);
        } catch (BankbookEntityException e) {
            resultDTOBuilder.requestBankbookRemainBalance(bankbook.getBalance());

            // TODO TransactionExceptionTranslator 추가 및 대체
            if (e instanceof BankbookMaximumBalanceLimitException)
                throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.MAXIMUM_BALANCE_REACHED);

            throw new TransactionException(resultDTOBuilder.build(), GlobalErrorCode.UNKNOWN_ERROR);
        } catch (OptimisticLockingFailureException e) {
            resultDTOBuilder.requestBankbookRemainBalance(bankbook.getBalance());
            throw new TransactionUnavailableException(resultDTOBuilder.build(), GlobalErrorCode.TRANSACTION_UNAVAILABLE);
        }

        resultDTOBuilder.requestBankbookRemainBalance(bankbook.getBalance());
        return resultDTOBuilder.build();
    }
}
