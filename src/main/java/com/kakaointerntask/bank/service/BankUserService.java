package com.kakaointerntask.bank.service;

import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.entity.enums.BankUserRank;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.BankbookNotFoundException;
import com.kakaointerntask.bank.exception.service.TransactionHistoryNotFoundException;
import com.kakaointerntask.bank.repository.BankUserRepository;
import com.kakaointerntask.bank.exception.service.BankUserNotFoundException;
import com.kakaointerntask.bank.repository.BankbookRepository;
import com.kakaointerntask.bank.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("bankUserService")
@RequiredArgsConstructor
public class BankUserService {
    private final BankUserRepository bankUserRepository;
    private final BankbookRepository bankbookRepository;

    @Transactional
    public void saveBankUserWithEncryptPassword(@Valid BankUser bankUser) {
        bankUser.replacePasswordEncrypted();
        bankUserRepository.save(bankUser);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void modifyBankUser(BankUser modifiedBankUser) {
        Objects.requireNonNull(modifiedBankUser, "Bank user entity should not be null");

        BankUser bankUser = findBankUserByUserNo(modifiedBankUser.getUserNo());

        bankUser.updateEntity(modifiedBankUser);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void modifyBankUserRank(Integer userNo, String rank) {
        BankUser bankUser = findBankUserByUserNo(userNo);
        bankUser.updateRank(BankUserRank.create(rank));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteBankUser(Integer userNo) {
        BankUser bankuser = findBankUserByUserNo(userNo);
        List<Bankbook> ownedBankbooks = bankbookRepository.findAllByOwnerUserNoAndActive(bankuser, true);

        /* 실제로 삭제는 하지 않고 isActive 플래그만 false로 만든다 */
        ownedBankbooks.stream().peek(Bankbook::inactive).collect(Collectors.toList());
        bankuser.inactive();
    }

    @Transactional(readOnly = true)
    public BankUser findBankUserByUserNo(Integer userNo) {
        return bankUserRepository.findByUserNoAndActive(userNo, true)
                .orElseThrow(() -> new BankUserNotFoundException(userNo, GlobalErrorCode.BANKUSER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public BankUser findBankUserById(String id) {
        return bankUserRepository.findBankUserByIdAndActive(id, true)
                .orElseThrow(() -> new BankUserNotFoundException(id, GlobalErrorCode.BANKUSER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<BankUser> findAllBankUsersExceptAdmin() {
        return new ArrayList<>(bankUserRepository.findAllByIdNotAndAdminAndActive("", false, true));
    }

    @Transactional(readOnly = true)
    public List<BankUser> findAllBankUsersExceptUserId(String currentUserId) {
        return new ArrayList<>(bankUserRepository.findAllByIdNotAndAdminAndActive(currentUserId, false, true));
    }

    @Transactional(readOnly = true)
    public boolean isIdBeingUsed(String id) {
        return bankUserRepository.existsById(id);
    }
}
