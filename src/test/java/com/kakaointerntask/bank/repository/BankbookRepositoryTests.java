package com.kakaointerntask.bank.repository;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import com.kakaointerntask.bank.exception.service.BankbookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class BankbookRepositoryTests extends AbstractRepositoryTests {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BankbookRepository bankbookRepository;

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void insert_test() {
        Bankbook bankbook = new Bankbook(
                null,
                "123123123123",
                BankUser.newInstanceWithUserNo(11),
                "새통장",
                BankbookType.NORMAL,
                0,
                true,
                TimestampUtil.now(),
                TimestampUtil.now(),
                null
        );

        Integer bookNo = bankbookRepository.save(bankbook).getBookNo();
        bankbookRepository.flush();
        Bankbook book = bankbookRepository.findById(bookNo)
                .orElseThrow(() -> new RuntimeException("Bank book not saved"));

        assertEquals(book.getBankbookId(), bankbook.getBankbookId());
        assertEquals(book.getOwnerUserNo().getUserNo(), bankbook.getOwnerUserNo().getUserNo());
        assertEquals(book.getAlias(), bankbook.getAlias());
        assertEquals(book.getType(), bankbook.getType());
        assertEquals(book.getInsertDate(), bankbook.getInsertDate());
        assertEquals(book.getUpdateDate(), bankbook.getUpdateDate());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void insert_all_test() {
        Bankbook bankbook1 = new Bankbook(
                null,
                "123123123123",
                BankUser.newInstanceWithUserNo(11),
                "새통장",
                BankbookType.NORMAL,
                0,
                true,
                TimestampUtil.now(),
                TimestampUtil.now(),
                null
        );

        Bankbook bankbook2 = new Bankbook(
                null,
                "123123123124",
                BankUser.newInstanceWithUserNo(13),
                "새통장2",
                BankbookType.NORMAL,
                0,
                true,
                TimestampUtil.now(),
                TimestampUtil.now(),
                null
        );

        Bankbook bankbook3 = new Bankbook(
                null,
                "123123123125",
                BankUser.newInstanceWithUserNo(14),
                "새통장3",
                BankbookType.CREDIT,
                0,
                true,
                TimestampUtil.now(),
                TimestampUtil.now(),
                null
        );

        List<Bankbook> bankbooks = new ArrayList<>();
        bankbooks.add(bankbook1);
        bankbooks.add(bankbook2);
        bankbooks.add(bankbook3);

        bankbookRepository.saveAll(bankbooks);
        bankbooks = bankbookRepository.findAll();
        assertEquals(3, bankbooks.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_test() {
        Bankbook bankbook = bankbookRepository.findById(19)
                .orElseThrow(() -> new RuntimeException("Bank book not found"));
        bankbookRepository.delete(bankbook);

        bankbook = bankbookRepository.findById(11).orElse(null);
        assertNull(bankbook);
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_all_test() {
        List<Bankbook> bankbooks = bankbookRepository.findAll();
        assertEquals(38, bankbooks.size());

        bankbookRepository.deleteAll(bankbooks);
        bankbooks = bankbookRepository.findAll();
        assertEquals(0, bankbooks.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void remove_some_test() {
        List<Bankbook> bankbooks = bankbookRepository.findAll();
        assertEquals(38, bankbooks.size());

        bankbooks.remove(0);
        bankbookRepository.deleteAll(bankbooks);
        bankbooks = bankbookRepository.findAll();
        assertEquals(1, bankbooks.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void modify_test() {
        Bankbook bankbook = bankbookRepository.findById(19)
                .orElseThrow(() -> new RuntimeException("Bank book not found"));
        assertTrue(bankbook.isActive());
        makeBookInactive(19);

        bankbookRepository.flush();
        entityManager.clear();

        bankbook = bankbookRepository.findById(19).orElse(null);
        assertNotNull(bankbook);
        assertFalse(bankbook.isActive());
    }

    public void makeBookInactive(Integer bookNo) {
        Bankbook bankbook = bankbookRepository.findById(19)
                .orElseThrow(() -> new RuntimeException("Bank book not found"));
        bankbook.inactive();
        bankbookRepository.saveAndFlush(bankbook);
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void modify_test_2() {
        Bankbook bankbook = bankbookRepository.findById(19)
                .orElseThrow(() -> new RuntimeException("Bank book not found"));
        long beforeBalance = bankbook.getBalance();
        int amount = 10000;
        bankbook.addBalance(amount);

        bankbookRepository.flush();

        Bankbook bankbook1 = bankbookRepository.findById(19).orElse(null);

        assertNotNull(bankbook1);
        assertEquals(beforeBalance + amount, bankbook1.getBalance());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    public void modify_flush_test() throws ParseException {
        Bankbook bankbook = bankbookRepository.findById(19)
                .orElseThrow(() -> new RuntimeException("Bank book not found"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bankbook.setUpdateDate(new Timestamp(format.parse("2020-01-01 00:00:00").getTime()));
        log.info(() -> "before flush");
        bankbookRepository.flush();
        log.info(() -> "after flush");

        bankbook = bankbookRepository.findById(19).orElse(null);
        assertNotNull(bankbook);
        assertEquals("2020-01-01 00:00:00", format.format(bankbook.getUpdateDate()));
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    @Transactional(propagation = Propagation.NEVER)
    public void modify_flush_propagation_never_test() throws ParseException {
        Bankbook bankbook = bankbookRepository.findById(19)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bankbook.setUpdateDate(new Timestamp(format.parse("2020-01-01 00:00:00").getTime()));
        bankbookRepository.saveAndFlush(bankbook);

        bankbook = bankbookRepository.findById(19).orElse(null);
        assertNotNull(bankbook);
        assertEquals("2020-01-01 00:00:00", format.format(bankbook.getUpdateDate()));
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    void findByBookNoAndActive() {
        assertNotNull(bankbookRepository.findByBookNoAndActive(19, true).orElse(null));
        assertNotNull(bankbookRepository.findByBookNoAndActive(20, true).orElse(null));
        assertNotNull(bankbookRepository.findByBookNoAndActive(21, true).orElse(null));
        assertNull(bankbookRepository.findByBookNoAndActive(22, true).orElse(null));
        assertNull(bankbookRepository.findByBookNoAndActive(19, false).orElse(null));
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    void findByBankbookIdAndActive() {
        assertNotNull(bankbookRepository.findByBankbookIdAndActive("1579587455180", true).orElse(null));
        assertNotNull(bankbookRepository.findByBankbookIdAndActive("1579587462369", true).orElse(null));
        assertNotNull(bankbookRepository.findByBankbookIdAndActive("1579587469507", true).orElse(null));
        assertNull(bankbookRepository.findByBankbookIdAndActive("1579587502605", true).orElse(null));
        assertNull(bankbookRepository.findByBankbookIdAndActive("1579587455180", false).orElse(null));
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    void findAllByOwnerUserNoAndActive() {
        assertEquals(6, bankbookRepository.findAllByOwnerUserNoAndActive(BankUser.newInstanceWithUserNo(11), true).size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    void findAllByOwnerUserNoAndTypeAndBalanceLessThanAndActive() {
        assertEquals(2, bankbookRepository.findAllByOwnerUserNoAndTypeAndBalanceLessThanAndActive(BankUser.newInstanceWithUserNo(11), BankbookType.CREDIT, 0L, true).size());
    }
}
