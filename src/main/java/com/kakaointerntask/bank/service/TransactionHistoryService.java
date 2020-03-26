package com.kakaointerntask.bank.service;

import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.dto.TransactionHistoryDTO;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import com.kakaointerntask.bank.exception.service.TransactionHistoryForbiddenException;
import com.kakaointerntask.bank.exception.service.TransactionHistoryNotFoundException;
import com.kakaointerntask.bank.repository.TransactionBookResultRepository;
import com.kakaointerntask.bank.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("transactionLogService")
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final BankUserService bankUserService;
    private final TransactionBookResultRepository transactionBookResultRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveTransactionHistory(TransactionHistory transactionHistory) {
        transactionBookResultRepository.save(transactionHistory.getRequestBookResultNo());
        if (transactionHistory.getReceivedBookResultNo() != null)
            transactionBookResultRepository.save(transactionHistory.getReceivedBookResultNo());
        transactionHistoryRepository.save(transactionHistory);
    }

    @Transactional(readOnly = true)
    public TransactionHistory findTransactionHistory(Long historyNo) {
        return transactionHistoryRepository.findById(historyNo).orElseThrow(
                () -> new TransactionHistoryNotFoundException(historyNo, GlobalErrorCode.HISTORY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public TransactionHistoryDTO findTransactonHistoryByBankUserNo(Long logNo, Integer bankUserNo) {
        TransactionHistory transactionHistory = findTransactionHistory(logNo);
        BankUser bankUser = bankUserService.findBankUserByUserNo(bankUserNo);
        if (!bankUser.equals(transactionHistory.getRequestUserNo()) && !bankUser.equals(transactionHistory.getReceivedUserNo()))
            throw new TransactionHistoryForbiddenException(logNo, GlobalErrorCode.HISTORY_ACCESS_FORBIDDEN);

        if (bankUser.equals(transactionHistory.getRequestUserNo()))
            return TransactionHistoryDTO.fromRequestLogEntity(transactionHistory);

        return TransactionHistoryDTO.fromReceivedLogEntity(transactionHistory);
    }

    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> findTransactionHistorysByBankUserNo(Integer bankUserNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(bankUserNo);
        List<TransactionHistory> logList = transactionHistoryRepository.findAllByRequestUserNoOrReceivedUserNo(bankUser, bankUser);
        return Stream.concat(logList.stream()
                                .filter(x -> x.getRequestUserNo().equals(bankUser))
                                .map(TransactionHistoryDTO::fromRequestLogEntity),
                            logList.stream()
                                .filter(x -> Objects.nonNull(x.getReceivedUserNo()))
                                .filter(x -> x.getReceivedUserNo().equals(bankUser))
                                .map(TransactionHistoryDTO::fromReceivedLogEntity))
                .sorted((log1, log2) -> TimestampUtil.compareTimestamp(log2.getTransactionDate(), log1.getTransactionDate()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer countSuccessfulTransferLogsOfThisMonthByBankuser(Integer bankUserNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(bankUserNo);
        return transactionHistoryRepository.countByRequestUserNoAndTypeAndTransactionDateBetween(
                bankUser,
                TransactionType.TRANSFER,
                TimestampUtil.getFirstTimestampOfMonth(TimestampUtil.now()),
                TimestampUtil.getLastTimestampOfMonth(TimestampUtil.now()));
    }

    @Transactional(readOnly = true)
    public boolean isBankUserOwnedTransactionLog(Integer userNo, Long historyNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);

        TransactionHistory transactionHistory = transactionHistoryRepository.findById(historyNo)
                .orElseThrow(() -> new TransactionHistoryNotFoundException(historyNo, GlobalErrorCode.HISTORY_NOT_FOUND));
        return bankUser.isOwned(transactionHistory);
    }
}
