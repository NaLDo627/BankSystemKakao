package com.kakaointerntask.bank.repository;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankUserRank;
import com.kakaointerntask.bank.exception.service.BankUserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class BankUserRepositoryTests extends AbstractRepositoryTests {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BankUserRepository bankUserRepository;

    @Test
    public void insert_test() {
        BankUser bankUser = new BankUser(
                null,
                "userid",
                "password",
                "이름",
                "email@example.com",
                "010-1234-5678",
                BankUserRank.NORMAL,
                false,
                true,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                null,
                null,
                null
        );

        Integer userNo = bankUserRepository.save(bankUser).getUserNo();
        BankUser user = bankUserRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("Bank user not saved"));

        assertEquals(user.getId(), bankUser.getId());
        assertEquals(user.getPassword(), bankUser.getPassword());
        assertEquals(user.getName(), bankUser.getName());
        assertEquals(user.getEmail(), bankUser.getEmail());
        assertEquals(user.getPhone(), bankUser.getPhone());
        assertEquals(user.getRank(), bankUser.getRank());
        assertEquals(user.isAdmin(), bankUser.isAdmin());
        assertEquals(user.isActive(), bankUser.isActive());
        assertEquals(user.getInsertDate(), bankUser.getInsertDate());
        assertEquals(user.getUpdateDate(), bankUser.getUpdateDate());
    }

    @Test
    public void insert_all_test() {
        BankUser bankUser1 = new BankUser(
                null,
                "userid",
                "password",
                "이름",
                "email@example.com",
                "010-1234-5678",
                BankUserRank.NORMAL,
                false,
                true,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                null,
                null,
                null
        );

        BankUser bankUser2 = new BankUser(
                null,
                "userid2",
                "password",
                "이름",
                "email@example.com",
                "010-1234-5678",
                BankUserRank.NORMAL,
                false,
                true,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                null,
                null,
                null
        );

        BankUser bankUser3 = new BankUser(
                null,
                "userid3",
                "password",
                "이름",
                "email@example.com",
                "010-1234-5678",
                BankUserRank.NORMAL,
                false,
                true,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()),
                null,
                null,
                null
        );

        List<BankUser> bankUsers = new ArrayList<>();
        bankUsers.add(bankUser1);
        bankUsers.add(bankUser2);
        bankUsers.add(bankUser3);

        bankUserRepository.saveAll(bankUsers);

        bankUsers = bankUserRepository.findAll();
        assertEquals(3, bankUsers.size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void remove_test() {
        BankUser bankUser = bankUserRepository.findById(11)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));
        bankUserRepository.delete(bankUser);

        bankUser = bankUserRepository.findById(11).orElse(null);
        assertNull(bankUser);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void remove_all_test() {
        List<BankUser> bankUsers = bankUserRepository.findAll();
        assertEquals(8, bankUsers.size());

        bankUserRepository.deleteAll(bankUsers);
        bankUsers = bankUserRepository.findAll();
        assertEquals(0, bankUsers.size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void remove_some_test() {
        List<BankUser> bankUsers = bankUserRepository.findAll();
        assertEquals(8, bankUsers.size());

        bankUsers.remove(0);
        bankUserRepository.deleteAll(bankUsers);
        bankUsers = bankUserRepository.findAll();
        assertEquals(1, bankUsers.size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void modify_test() {
        BankUser bankUser = bankUserRepository.findById(11)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));
        bankUser.setUpdateDate(null);

        bankUser = bankUserRepository.findById(11).orElse(null);
        assertNotNull(bankUser);
        assertNull(bankUser.getUpdateDate());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void modify_flush_test() throws ParseException {
        BankUser bankUser = bankUserRepository.findById(11)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bankUser.setUpdateDate(new Timestamp(format.parse("2020-01-01 00:00:00").getTime()));
        log.info(() -> "before flush");
        bankUserRepository.flush();
        log.info(() -> "after flush");

        bankUser = bankUserRepository.findById(11).orElse(null);
        assertNotNull(bankUser);
        assertEquals("2020-01-01 00:00:00", format.format(bankUser.getUpdateDate()));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @Transactional(propagation = Propagation.NEVER)
    public void modify_flush_propagation_never_test() throws ParseException {
        BankUser bankUser = bankUserRepository.findById(11)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bankUser.setUpdateDate(new Timestamp(format.parse("2020-01-01 00:00:00").getTime()));
        bankUserRepository.saveAndFlush(bankUser);

        bankUser = bankUserRepository.findById(11).orElse(null);
        assertNotNull(bankUser);
        assertEquals("2020-01-01 00:00:00", format.format(bankUser.getUpdateDate()));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    void findTest() {
        BankUser bankUser = bankUserRepository.findById(11)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        assertEquals("user", bankUser.getId());
        assertEquals("사용자", bankUser.getName());
        assertEquals("test@example.com", bankUser.getEmail());
        assertEquals("010-1234-5678", bankUser.getPhone());
        assertEquals(BankUserRank.NORMAL, bankUser.getRank());
        assertFalse(bankUser.isAdmin());
        assertTrue(bankUser.isActive());
        assertEquals("2020-02-03 13:16:22", format.format(bankUser.getInsertDate()));
        assertEquals("2020-02-04 10:10:25", format.format(bankUser.getUpdateDate()));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    void existsById() {
        assertTrue(bankUserRepository.existsById("user"));
        assertFalse(bankUserRepository.existsById("non-exist-user"));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    void findByUserNoAndActive() {
        assertNotNull(bankUserRepository.findByUserNoAndActive(11, true).orElse(null));
        assertNotNull(bankUserRepository.findByUserNoAndActive(12, true).orElse(null));
        assertNotNull(bankUserRepository.findByUserNoAndActive(13, true).orElse(null));
        assertNull(bankUserRepository.findByUserNoAndActive(18, true).orElse(null));
        assertNull(bankUserRepository.findByUserNoAndActive(11, false).orElse(null));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    void findBankUserByIdAndActive() {
        assertNotNull(bankUserRepository.findBankUserByIdAndActive("user", true).orElse(null));
        assertNotNull(bankUserRepository.findBankUserByIdAndActive("vipuser", true).orElse(null));
        assertNotNull(bankUserRepository.findBankUserByIdAndActive("vvipuser", true).orElse(null));
        assertNull(bankUserRepository.findBankUserByIdAndActive("inactiveuser", true).orElse(null));
        assertNull(bankUserRepository.findBankUserByIdAndActive("user", false).orElse(null));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/bankuser_dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    void findAllByIdNotAndAdminAndActive() {
        List<BankUser> bankUsers = bankUserRepository.findAllByIdNotAndAdminAndActive("user", false, true);
        assertEquals(5, bankUsers.size());
    }

    @Test
    @DatabaseSetup(value = {"classpath:dbunit/bankuser_dataset.xml", "classpath:dbunit/bankbook_dataset.xml"}, type = DatabaseOperation.CLEAN_INSERT)
    void testLazyFetch() {
        BankUser bankUser = bankUserRepository.findBankUserByIdAndActive("user", true)
                .orElseThrow(() -> new RuntimeException("Bank user not found"));
        List<Bankbook> bankbooks = bankUser.getOwnedBankbooks();
        Integer size = bankbooks.size();
        assertEquals(25, size);
    }
}