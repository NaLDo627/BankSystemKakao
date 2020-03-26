package com.kakaointerntask.bank.repository;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.TransactionBookResult;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import com.kakaointerntask.bank.exception.service.TransactionHistoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class TransactionHistoryRepositoryTests extends AbstractRepositoryTests {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TransactionBookResultRepository transactionBookResultRepository;

    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void insert_transactionLog_test() {
        TransactionHistory transactionHistory = new TransactionHistory(
                null,
                BankUser.newInstanceWithUserNo(11),
                null,
                null,
                null,
                100,
                101,
                102,
                TransactionType.WITHDRAW,
                "memo",
                TimestampUtil.now()
        );

        Long logNo = transactionHistoryRepository.save(transactionHistory).getHistoryNo();
        TransactionHistory log = transactionHistoryRepository.findById(logNo)
                .orElseThrow(() -> new RuntimeException("TransactionLog not saved"));

        assertEquals(log.getRequestUserNo().getUserNo(), transactionHistory.getRequestUserNo().getUserNo());
        assertEquals(log.getRequestAmount(), transactionHistory.getRequestAmount());
        assertEquals(log.getTransactionFee(), transactionHistory.getTransactionFee());
        assertEquals(log.getActualAmount(), transactionHistory.getActualAmount());
        assertEquals(log.getType(), transactionHistory.getType());
        assertEquals(log.getMemo(), transactionHistory.getMemo());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void insert_transactionLog_all_test() {
        TransactionHistory transactionHistory1 = new TransactionHistory(
                null,
                BankUser.newInstanceWithUserNo(11),
                null,
                null,
                null,
                1001,
                1011,
                1021,
                TransactionType.WITHDRAW,
                "memo1",
                TimestampUtil.now()
        );

        TransactionHistory transactionHistory2 = new TransactionHistory(
                null,
                BankUser.newInstanceWithUserNo(11),
                null,
                null,
                null,
                1002,
                1012,
                1022,
                TransactionType.WITHDRAW,
                "memo2",
                TimestampUtil.now()
        );

        TransactionHistory transactionHistory3 = new TransactionHistory(
                null,
                BankUser.newInstanceWithUserNo(11),
                null,
                null,
                null,
                1002,
                1012,
                1022,
                TransactionType.WITHDRAW,
                "memo1",
                TimestampUtil.now()
        );

        List<TransactionHistory> transactionHistories = new ArrayList<>();
        transactionHistories.add(transactionHistory1);
        transactionHistories.add(transactionHistory2);
        transactionHistories.add(transactionHistory3);

        transactionHistoryRepository.saveAll(transactionHistories);
        transactionHistories = transactionHistoryRepository.findAll();
        assertEquals(3, transactionHistories.size());
    }

/*    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/transaction_log_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_transactionLog_test() {
        TransactionLog transactionLog = transactionLogDAO.findById(1L)
                .orElseThrow(() -> new TransactionLogNotFoundException("Transaction Log not found"));
        transactionLogDAO.delete(transactionLog);

        transactionLog = transactionLogDAO.findById(1L).orElse(null);
        assertNull(transactionLog);
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/transaction_log_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_all_transactionLog_test() {
        List<TransactionLog> transactionLogs = transactionLogDAO.findAll();
        assertEquals(19, transactionLogs.size());

        transactionLogDAO.deleteAll(transactionLogs);
        transactionLogs = transactionLogDAO.findAll();
        assertEquals(0, transactionLogs.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/transaction_log_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_some_transactionLog_test() {
        List<TransactionLog> transactionLogs = transactionLogDAO.findAll();
        assertEquals(19, transactionLogs.size());

        transactionLogs.remove(0);
        transactionLogDAO.deleteAll(transactionLogs);
        transactionLogs = transactionLogDAO.findAll();
        assertEquals(1, transactionLogs.size());
    }*/

    @Test
    void countByRequestUserNoAndTypeAndResultCodeAndTransactionDateBetween() {
    }

    @Test
    void findAllByRequestUserNoOrReceivedUserNo() {
    }

    @Test
    public void insert_transactionBookResult_test() {
        TransactionBookResult transactionBookResult = new TransactionBookResult(
                null,
                "123123123",
                "통장이름",
                100000,
                1111111
        );

        Long bookResultNo = transactionBookResultRepository.save(transactionBookResult).getBookResultNo();
        TransactionBookResult result = transactionBookResultRepository.findById(bookResultNo)
                .orElseThrow(() -> new RuntimeException("TransactionLog not saved"));

        assertEquals(transactionBookResult.getTransactionBookId(), result.getTransactionBookId());
        assertEquals(transactionBookResult.getTransactionBookAlias(), result.getTransactionBookAlias());
        assertEquals(transactionBookResult.getPreviousBalance(), result.getPreviousBalance());
        assertEquals(transactionBookResult.getRemainBalance(), result.getRemainBalance());
    }

    @Test
    public void insert_transactionBookResult_all_test() {
        TransactionBookResult transactionBookResult1 = new TransactionBookResult(
                null,
                "1231231231231",
                "통장이름1",
                10000011,
                111111111
        );

        TransactionBookResult transactionBookResult2 = new TransactionBookResult(
                null,
                "1231231231232",
                "통장이름2",
                10000012,
                111111112
        );

        TransactionBookResult transactionBookResult3 = new TransactionBookResult(
                null,
                "1231231231233",
                "통장이름3",
                10000013,
                111111113
        );

        List<TransactionBookResult> transactionBookResults = new ArrayList<>();
        transactionBookResults.add(transactionBookResult1);
        transactionBookResults.add(transactionBookResult2);
        transactionBookResults.add(transactionBookResult3);

        transactionBookResultRepository.saveAll(transactionBookResults);
        transactionBookResults = transactionBookResultRepository.findAll();
        assertEquals(3, transactionBookResults.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/transaction_book_result_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_transactionBookResult_test() {
        TransactionBookResult transactionBookResult = transactionBookResultRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Transaction Log not found"));
        transactionBookResultRepository.delete(transactionBookResult);

        transactionBookResult = transactionBookResultRepository.findById(1L).orElse(null);
        assertNull(transactionBookResult);
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/transaction_book_result_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_all_transactionBookResult_test() {
        List<TransactionBookResult> transactionBookResults = transactionBookResultRepository.findAll();
        assertEquals(22, transactionBookResults.size());

        transactionBookResultRepository.deleteAll(transactionBookResults);
        transactionBookResults = transactionBookResultRepository.findAll();
        assertEquals(0, transactionBookResults.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/transaction_book_result_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_some_transactionBookResult_test() {
        List<TransactionBookResult> transactionBookResults = transactionBookResultRepository.findAll();
        assertEquals(22, transactionBookResults.size());

        transactionBookResults.remove(0);
        transactionBookResultRepository.deleteAll(transactionBookResults);
        transactionBookResults = transactionBookResultRepository.findAll();
        assertEquals(1, transactionBookResults.size());
    }
}