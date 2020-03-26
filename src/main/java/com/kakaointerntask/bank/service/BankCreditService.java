package com.kakaointerntask.bank.service;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.service.BankUserCreditLimitReachedException;
import com.kakaointerntask.bank.repository.BankbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankCreditService {
    private final BankUserService bankUserService;
    private final BankbookRepository bankbookRepository;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public int getRemainCreditLimitByBankUserNo(Integer bankUserNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(bankUserNo);
        List<Bankbook> creditBankbooks =
                bankbookRepository.findAllByOwnerUserNoAndTypeAndBalanceLessThanAndActive(
                        bankUser, BankbookType.CREDIT, 0L, true);
        int limit = bankUser.getRank().getCreditLimit();

        if(CollectionUtils.isEmpty(creditBankbooks))
            return limit;

        /* long -> int 로 캐스팅 하여도 한도의 최대는 1000 만원이기 때문에 문제 없다 */
        // TODO entityManager.refresh()
        int totalLoan = (int)(creditBankbooks.stream()
                .map(Bankbook::getBalance)
                .reduce(0L, Long::sum) * -1L);

        return Math.max(limit - totalLoan, 0);
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public boolean isBankUserReachedCreditLimit(Integer bankUserNo) {
        return getRemainCreditLimitByBankUserNo(bankUserNo) == 0;
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public void checkBankUserReachedCreditLimit(Integer bankUserNo) {
        if (isBankUserReachedCreditLimit(bankUserNo))
            throw new BankUserCreditLimitReachedException(bankUserNo, GlobalErrorCode.BANKUSER_MAXIMUM_CREDIT_REACHED);
    }
}
