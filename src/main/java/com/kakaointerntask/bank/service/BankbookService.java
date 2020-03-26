package com.kakaointerntask.bank.service;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.dto.BankbookDTO;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.*;
import com.kakaointerntask.bank.repository.BankbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service("bankbookService")
@RequiredArgsConstructor
public class BankbookService {
    private final BankbookRepository bankbookRepository;
    private final BankUserService bankUserService;
    private final BankCreditService bankCreditService;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveBankbookFromDTO(@Valid BankbookDTO bankbookDTO) {
         BankUser bankUser = bankUserService.findBankUserByUserNo(bankbookDTO.getOwnerUserNo());
         saveBankbook(bankbookDTO.toNewEntity(bankUser));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void modifyBankbookType(Integer bookNo, String type) {
        Bankbook bankbook = findBankbookByBookNo(bookNo);
        // TODO entityManager.refresh()
        bankbook.updateType(BankbookType.create(type));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveBankbook(@Valid Bankbook bankbook) {
        bankCreditService.checkBankUserReachedCreditLimit(bankbook.getOwnerUserNo().getUserNo());
        bankbookRepository.save(bankbook);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteBankbook(Integer bookNo, boolean forceDelete) {
        Bankbook bankbook = findBankbookByBookNo(bookNo);
        if (bankbook.getBalance() != 0 && !forceDelete)
            throw new BankbookForbiddenException(bookNo, GlobalErrorCode.BANKBOOK_DELETE_BALANCE_NOT_0);

        /* 실제로 삭제는 하지 않고 isActive 플래그만 false로 만든다 */
        bankbook.inactive();
    }

    @Transactional(readOnly = true)
    public List<Bankbook> findAllBankbooks() {
        return bankbookRepository.findAllByActive(true);
    }

    @Transactional(readOnly = true)
    public Bankbook findBankbookByBankbookId(String bankbookId) {
        return bankbookRepository.findByBankbookIdAndActive(bankbookId, true)
                .orElseThrow(() -> new BankbookNotFoundException(bankbookId, GlobalErrorCode.BANKBOOK_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Bankbook findBankbookByBookNo(Integer bookNo) {
        return bankbookRepository.findByBookNoAndActive(bookNo, true)
                .orElseThrow(() -> new BankbookNotFoundException(bookNo, GlobalErrorCode.BANKBOOK_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Bankbook> findBankbooksByBankUserNo(Integer bankUserNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(bankUserNo);
        return bankbookRepository.findAllByOwnerUserNoAndActive(bankUser, true);
    }

    @Transactional(readOnly = true)
    public boolean isBankUserOwnedBankbook(Integer userNo, Integer bookNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        Bankbook bankbook = bankbookRepository.findById(bookNo)
                .orElseThrow(() -> new BankbookNotFoundException(bookNo, GlobalErrorCode.BANKBOOK_NOT_FOUND));
        return bankUser.isOwned(bankbook);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void addBalanceToBankbook(Integer bookNo, long amount) {
        Bankbook bankbook = findBankbookByBookNo(bookNo);
        bankbook.addBalance(amount);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void subBalanceToBankbook(Integer bookNo, long amount) {
        Bankbook bankbook = findBankbookByBookNo(bookNo);

        long balance = bankbook.getBalance();
        if (balance - amount < 0L && bankbook.getType() == BankbookType.CREDIT) { // TODO BankCreditSerivce 에서 체크
            int remainCreditLimit = bankCreditService.getRemainCreditLimitByBankUserNo(bankbook.getOwnerUserNo().getUserNo());

            if (remainCreditLimit + balance - amount < 0)
                throw new BankMaximumCreditLimitException(bookNo, GlobalErrorCode.BANKBOOK_MAXIMUM_CREDIT_LIMIT_REACHED);
        }

        bankbook.subBalance(amount);
    }
}
