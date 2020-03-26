package com.kakaointerntask.bank.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.kakaointerntask.bank.BankApplication;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.dto.TransactionRequestDTO;
import com.kakaointerntask.bank.repository.BankbookRepository;
import com.kakaointerntask.bank.service.implement.TransactionDepositService;
import org.junit.After;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApplication.class)
@ContextConfiguration
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class }, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:transaction-test.properties")
class TransactionDepositTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BankbookRepository bankbookRepository;

    @Autowired
    TransactionDepositService transactionService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void DB_정상적으로_연결되었는지_확인() {
        Bankbook asisBankbook = bankbookRepository.findById(21).orElse(null);
        assertNotNull(asisBankbook);
    }

    @Test
    void handleTransactionRequest_MultiThread() throws Throwable {
        expectedException.expect(OptimisticLockingFailureException.class);
        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());


        Integer userNo = 11;
        Integer bookNo = 24;
        String bankbookId = "1579588652740";
        Integer amount = 10000;

        int concurrency = 4;
        List<Callable<String>> concurrencyList = new ArrayList<>();
        for (long i = 1L; i <= concurrency; i++) {
            concurrencyList.add(handleTransactionWithdraw(userNo, bankbookId, amount));
        }

        Bankbook asisBankbook = bankbookRepository.findById(bookNo).orElse(null);
        assert asisBankbook != null;
        Long expectedCash = asisBankbook.getBalance() + amount;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Future<String>> futureList = executorService.invokeAll(concurrencyList);
        OptimisticLockingFailureException thrown = null;
        for (Future<String> future : futureList) {
            try {
                future.get();
            } catch (ExecutionException e) {
                log.error(e, () -> "exception occurred");
                thrown = assertThrows(OptimisticLockingFailureException.class, () -> {throw e.getCause();});
            }
        }

        assertNotNull(thrown);

        // To-Be wallet cash 가져오기
        Bankbook tobeBankbook = bankbookRepository.findById(bookNo).orElse(null);

        // concurrency만큼 차감되었는지 확인
        assert tobeBankbook != null;
        assertEquals(expectedCash, tobeBankbook.getBalance());

    }

    @Test
    void handleTransactionRequest_MultiThread_ReadCommited() throws Throwable {
        expectedException.expect(OptimisticLockingFailureException.class);
        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());


        Integer userNo = 11;
        Integer bookNo = 24;
        String bankbookId = "1579588652740";
        Integer amount = 10000;

        int concurrency = 4;
        List<Callable<String>> concurrencyList = new ArrayList<>();
        for (long i = 1L; i <= concurrency; i++) {
            concurrencyList.add(handleTransactionWithdrawReadCommited(userNo, bankbookId, amount));
        }

        Bankbook asisBankbook = bankbookRepository.findById(bookNo).orElse(null);
        assert asisBankbook != null;
        Long expectedCash = asisBankbook.getBalance() + amount;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Future<String>> futureList = executorService.invokeAll(concurrencyList);
        OptimisticLockingFailureException thrown = null;
        for (Future<String> future : futureList) {
            try {
                future.get();
            } catch (ExecutionException e) {
                log.error(e, () -> "exception occurred");
                thrown = assertThrows(OptimisticLockingFailureException.class, () -> {throw e.getCause();});
            }
        }

        assertNotNull(thrown);

        // To-Be wallet cash 가져오기
        Bankbook tobeBankbook = bankbookRepository.findById(bookNo).orElse(null);

        // concurrency만큼 차감되었는지 확인
        assert tobeBankbook != null;
        assertEquals(expectedCash, tobeBankbook.getBalance());

    }

    @Test
    void handleTransactionRequest_MultiThread_RepeatableRead() throws Throwable {
        expectedException.expect(OptimisticLockingFailureException.class);
        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());


        Integer userNo = 11;
        Integer bookNo = 24;
        String bankbookId = "1579588652740";
        Integer amount = 10000;

        int concurrency = 4;
        List<Callable<String>> concurrencyList = new ArrayList<>();
        for (long i = 1L; i <= concurrency; i++) {
            concurrencyList.add(handleTransactionWithdrawRepeatableRead(userNo, bankbookId, amount));
        }

        Bankbook asisBankbook = bankbookRepository.findById(bookNo).orElse(null);
        assert asisBankbook != null;
        Long expectedCash = asisBankbook.getBalance() + amount;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Future<String>> futureList = executorService.invokeAll(concurrencyList);
        OptimisticLockingFailureException thrown = null;
        for (Future<String> future : futureList) {
            try {
                future.get();
            } catch (ExecutionException e) {
                log.error(e, () -> "exception occurred");
                thrown = assertThrows(OptimisticLockingFailureException.class, () -> {throw e.getCause();});
            }
        }

        assertNotNull(thrown);

        // To-Be wallet cash 가져오기
        Bankbook tobeBankbook = bankbookRepository.findById(bookNo).orElse(null);

        // concurrency만큼 차감되었는지 확인
        assert tobeBankbook != null;
        assertEquals(expectedCash, tobeBankbook.getBalance());

    }

    public Callable<String> handleTransactionWithdraw(Integer requestUserNo, String transactionBookId, Integer amount) {
        return () -> {

            TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                    requestUserNo,
                    transactionBookId,
                    null,
                    amount,
                    null
            );

            transactionService.handleTransactionRequest(requestDTO);
            return "success";
        };
    }



    public Callable<String> handleTransactionWithdrawReadCommited(Integer requestUserNo, String transactionBookId, Integer amount) {
        return () -> {

            TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                    requestUserNo,
                    transactionBookId,
                    null,
                    amount,
                    null
            );

            TransactionalWrapper wrapper = new TransactionalWrapper(transactionService);

            wrapper.depositReadCommitedWrapper(requestDTO);
            return "success";
        };
    }

    public Callable<String> handleTransactionWithdrawRepeatableRead(Integer requestUserNo, String transactionBookId, Integer amount) {
        return () -> {

            TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                    requestUserNo,
                    transactionBookId,
                    null,
                    amount,
                    null
            );

            TransactionalWrapper wrapper = new TransactionalWrapper(transactionService);

            wrapper.depositRepeatableReadWrapper(requestDTO);
            return "success";
        };
    }
}

@Service
class TransactionalWrapper {
    Logger log = LoggerFactory.getLogger(this.getClass());

    TransactionDepositService transactionService;

    TransactionalWrapper(TransactionDepositService transactionDepositService) {
        this.transactionService = transactionDepositService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void depositReadCommitedWrapper(TransactionRequestDTO requestDTO) {
        transactionService.handleTransactionRequest(requestDTO);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void depositRepeatableReadWrapper(TransactionRequestDTO requestDTO) {
        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        transactionService.handleTransactionRequest(requestDTO);
    }
}