package com.kakaointerntask.bank.repository;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import com.kakaointerntask.bank.exception.service.BankbookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:transaction-test.properties")
class BankbookRepositoryConcurrencyTests extends AbstractRepositoryTests {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BankbookRepository bankbookRepository;

    @Test
   // @Transactional(propagation = Propagation.NOT_SUPPORTED) // Transaction 을 사용하지 않아야 작동한다???
     @Transactional(isolation = Isolation.READ_COMMITTED)
    // @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    void concurrencyTest() throws InterruptedException {
        int concurrency = 4;
        int amount = 10000;
        Integer bookNo = 21;

        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        List<Callable<String>> concurrencyList = new ArrayList<>();
        for (long i = 1L; i <= concurrency; i++) {
            concurrencyList.add(addBalanceCallable(bookNo, amount));
        }

        //Bankbook asisBankbook = bankbookRepository.findById(bookNo).orElse(null);
        Bankbook asisBankbook = findBankbook(bookNo);
        assert asisBankbook != null;
        log.info(() -> "current balance : " + asisBankbook.getBalance());
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

        //Bankbook tobeBankbook = bankbookRepository.findByBookNoAndActive(bookNo, true).orElse(null);

        entityManager.clear();
        Bankbook tobeBankbook = findBankbook(bookNo); // find 든 jpql 이든 db로는 날아가지만 repetable read일 경우 변경된 내용 가져오지 않음
        //entityManager.refresh(tobeBankbook);

        // concurrency만큼 차감되었는지 확인
        assert tobeBankbook != null;
        assertEquals(expectedCash, tobeBankbook.getBalance());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Bankbook findBankbook(Integer bookNo) {
        return bankbookRepository.findById(bookNo).orElseThrow(() -> new RuntimeException("not exist"));
    }

    public Callable<String> addBalanceCallable(int bookNo, int amount) {
        return () -> {
            addBalance(bookNo, amount);
            return "yes";
        };
    }

    public void addBalance(int bookNo, int amount) {
        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        Bankbook bankbook = bankbookRepository.findById(bookNo).orElseThrow(() -> new RuntimeException("bankbook not found"));
        bankbook.addBalance(amount);
        Integer version = bankbook.getVersion();
        Integer finalVersion = version;
        log.info(() -> "current version : " + finalVersion);
        bankbook = bankbookRepository.saveAndFlush(bankbook);
        version = bankbook.getVersion();
        Integer finalVersion1 = version;
        log.info(() -> "current version : " + finalVersion1);
    }

    @Test
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void optimisticLockTestIsolationReadCommitted() throws InterruptedException {
        int concurrency = 4;
        int amount = 10000;
        Integer bookNo = 21;

        log.info(() -> "current transaction : " + TransactionSynchronizationManager.getCurrentTransactionName());
        List<Callable<String>> concurrencyList = new ArrayList<>();
        for (long i = 1L; i <= concurrency; i++) {
            concurrencyList.add(addBalanceCallable(bookNo, amount));
        }

        //Bankbook asisBankbook = bankbookRepository.findById(bookNo).orElse(null);
        Bankbook asisBankbook = findBankbook(bookNo);
        assert asisBankbook != null;
        log.info(() -> "current balance : " + asisBankbook.getBalance());
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

        //Bankbook tobeBankbook = bankbookRepository.findByBookNoAndActive(bookNo, true).orElse(null);

        entityManager.clear();
        Bankbook tobeBankbook = findBankbook(bookNo); // find 든 jpql 이든 db로는 날아가지만 repetable read일 경우 변경된 내용 가져오지 않음
        //entityManager.refresh(tobeBankbook);

        // concurrency만큼 차감되었는지 확인
        assert tobeBankbook != null;
        assertEquals(expectedCash, tobeBankbook.getBalance());
    }
}
