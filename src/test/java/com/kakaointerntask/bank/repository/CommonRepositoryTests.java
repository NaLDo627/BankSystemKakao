package com.kakaointerntask.bank.repository;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:transaction-test.properties")
class CommonRepositoryTests extends AbstractRepositoryTests {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BankUserRepository bankUserRepository;

    @Test
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public void flush_test() {
        log.info(() -> "current Transaction isoloation : " + TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());

        // select query
        BankUser bankUser = entityManager.find(BankUser.class, 11);

        // nothing happened
        bankUser = entityManager.find(BankUser.class, 11);

        // get breaking point -> do update db with native query
        int tmp = 0;

        bankUser = entityManager.find(BankUser.class, 11);

        entityManager.flush();

        bankUser = entityManager.find(BankUser.class, 11);
        bankUser.getId();

        entityManager.refresh(bankUser);

        bankUser = entityManager.find(BankUser.class, 11);
        bankUser.getId();
    }
}
