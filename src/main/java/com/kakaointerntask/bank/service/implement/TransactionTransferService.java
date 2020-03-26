package com.kakaointerntask.bank.service.implement;

import com.kakaointerntask.bank.dto.TransactionRequestDTO;
import com.kakaointerntask.bank.dto.TransactionResultDTO;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankUserRank;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import com.kakaointerntask.bank.exception.controller.RequestBadRequestException;
import com.kakaointerntask.bank.exception.entity.BankbookMaximumBalanceLimitException;
import com.kakaointerntask.bank.exception.entity.BankbookNotEnoughBalanceException;
import com.kakaointerntask.bank.exception.service.*;
import com.kakaointerntask.bank.exception.service.transaction.TransactionBadRequestException;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import javax.validation.Valid;

@Slf4j
@Service("transactionTransferService")
@RequiredArgsConstructor
public class TransactionTransferService implements TransactionService {
    private final BankUserService bankUserService;
    private final BankbookService bankbookService;
    private final TransactionHistoryService transactionHistoryService;

    private final double TRANSFER_CHARGE_RATE = 0.05;

    // @TODO 요청 검증 로직 모듈화
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransactionResultDTO handleTransactionRequest(@Valid TransactionRequestDTO requestDTO) {
        log.info("current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        Bankbook bankbook = bankbookService.findBankbookByBankbookId(requestDTO.getTransactionBookId());

        if (StringUtils.isEmpty(requestDTO.getReceiverBookId()))
            throw new RequestBadRequestException(GlobalErrorCode.RECEIVER_BANKBOOK_NOT_FOUND);

        if (requestDTO.getTransactionBookId().equals(requestDTO.getReceiverBookId()))
            throw new RequestBadRequestException(GlobalErrorCode.RECEIVER_BANKBOOK_AND_SENDER_BANKBOOK_ARE_SAME);

        try {
            Bankbook receiverBankbook = bankbookService.findBankbookByBankbookId(requestDTO.getReceiverBookId());
            TransactionResultDTO resultDTO = handleTransactionTransfer(requestDTO, bankbook, receiverBankbook);
            transactionHistoryService.saveTransactionHistory(resultDTO.toHistoryEntity());
            return resultDTO;
        } catch (BankbookNotFoundException e) {
            throw new RequestBadRequestException(GlobalErrorCode.RECEIVER_BANKBOOK_NOT_FOUND);
        }
    }

    private TransactionResultDTO handleTransactionTransfer(TransactionRequestDTO requestDTO,
                                                             Bankbook senderBankbook,
                                                             Bankbook receiverBankbook) {
        TransactionResultDTO.TransactionResultDTOBuilder resultDTOBuilder = TransactionResultDTO.builder()
                .requestUserNo(requestDTO.getRequestUserNo())
                .receivedUserNo(receiverBankbook.getOwnerUserNo().getUserNo())
                .requestBankbookId(senderBankbook.getBankbookId())
                .receivedBankbookId(receiverBankbook.getBankbookId())
                .requestBankbookAlias(senderBankbook.getAlias())
                .receivedBankbookAlias(receiverBankbook.getAlias())
                .requestBankbookPreviousBalance(senderBankbook.getBalance())
                .receivedBankbookPreviousBalance(receiverBankbook.getBalance())
                .requestAmount(requestDTO.getAmount())
                .type(TransactionType.TRANSFER)
                .memo(requestDTO.getMemo());

        int transferFee = getTransactionFee(requestDTO, senderBankbook.getOwnerUserNo());
        int senderTransferAmount = requestDTO.getAmount() + transferFee;
        int receiverTransferAmount = requestDTO.getAmount();

        // @TODO 수수료 로직 모듈화
        resultDTOBuilder.transactionFee(transferFee);
        resultDTOBuilder.actualAmount(senderTransferAmount);

        if (senderTransferAmount > 500000000) {
            resultDTOBuilder.requestBankbookRemainBalance(senderBankbook.getBalance());
            resultDTOBuilder.receivedBankbookRemainBalance(receiverBankbook.getBalance());
            throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.MAXIMUM_TRANSACTION_AMOUNT_REACHED);
        }

        try {
            bankbookService.subBalanceToBankbook(senderBankbook.getBookNo(), senderTransferAmount);
            bankbookService.addBalanceToBankbook(receiverBankbook.getBookNo(), receiverTransferAmount);
        } catch (RuntimeException e) {
            /* 연산 중 예외 발생시 현재 트랜잭션 강제 롤백 */
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            resultDTOBuilder.requestBankbookRemainBalance(senderBankbook.getBalance());
            resultDTOBuilder.receivedBankbookRemainBalance(receiverBankbook.getBalance());

            // TODO TransactionExceptionTranslator 추가 및 대체
            if (e instanceof BankbookNotEnoughBalanceException)
                throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.NOT_ENOUGH_BALANCE);
            else if (e instanceof BankMaximumCreditLimitException)
                throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.MAXIMUM_CREDIT_REACHED);
            else if (e instanceof BankbookMaximumBalanceLimitException)
                throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.RECEIVER_MAXIMUM_BALANCE_REACHED);
            else if (e instanceof OptimisticLockingFailureException)
                throw new TransactionUnavailableException(resultDTOBuilder.build(), GlobalErrorCode.TRANSACTION_UNAVAILABLE);

            throw new TransactionBadRequestException(resultDTOBuilder.build(), GlobalErrorCode.UNKNOWN_ERROR);
        }

        resultDTOBuilder.requestBankbookRemainBalance(senderBankbook.getBalance());
        resultDTOBuilder.receivedBankbookRemainBalance(receiverBankbook.getBalance());
        return resultDTOBuilder.build();
    }

    // @TODO 수수료 발생 로직 독립화
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public boolean isTransferChargeOccurs(Integer userNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);

        if (bankUser.getRank() == BankUserRank.VVIP)
            return false;

        if (bankUser.getRank() == BankUserRank.VIP) {
            // entityManager.clear() ??
            Integer transactionCountOfThisMonth = transactionHistoryService.countSuccessfulTransferLogsOfThisMonthByBankuser(userNo);

            if (transactionCountOfThisMonth < 3)
                return false;
        }
        return true;
    }

    private int getTransactionFee(TransactionRequestDTO requestDTO, BankUser bankUser) {
        if (!isTransferChargeOccurs(bankUser.getUserNo()))
            return 0;

        /* 소수점 이하는 버린다 */
        return (int)(requestDTO.getAmount() * TRANSFER_CHARGE_RATE);
    }
}
