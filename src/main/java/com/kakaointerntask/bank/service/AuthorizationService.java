package com.kakaointerntask.bank.service;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import com.kakaointerntask.bank.exception.service.BankbookForbiddenException;
import com.kakaointerntask.bank.exception.service.TransactionHistoryForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    // @TODO User 조회를 service를 통한 JPQL 사용 대신 findbyId 메소드 사용 (영속성 컨텍스트 사용)
    private final BankUserService bankUserService;
    private final BankbookService bankbookService;
    private final TransactionHistoryService transactionHistoryService;

    @Transactional(readOnly = true)
    public void checkAdminInSession(HttpSession session) {
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        checkUserAdmin(bankUser.getUserNo());
    }

    @Transactional(readOnly = true)
    public void checkUserAdmin(Integer userNo) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        if (!bankUser.isAdmin())
            throw new OperationNotPermittedException("Admin confirmation failed");
    }

    @Transactional(readOnly = true)
    public void checkBankUserAccessibility(Integer userNo, HttpSession session) {
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        if (bankUser.isAdmin())
            return;

        checkRequestUserInSession(userNo, session);
    }

    @Transactional(readOnly = true)
    public void checkRequestUserInSession(Integer userNo, HttpSession session) {
        Objects.requireNonNull(userNo);

        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        if (!userNo.equals(bankUser.getUserNo()))
            throw new OperationNotPermittedException("User not in session");
    }

    @Transactional(readOnly = true)
    public void checkBankbookAccessibility(String bookId, Integer userNo, HttpSession session) {
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        if (bankUser.isAdmin())
            return;

        checkBankbookOwner(bookId, userNo);
    }

    @Transactional(readOnly = true)
    public void checkBankbookOwner(String bookId, Integer userNo) {
        Bankbook bankbook = bankbookService.findBankbookByBankbookId(bookId);
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        if (!bankUser.isOwned(bankbook))
            throw new BankbookForbiddenException(bankbook.getBookNo(), GlobalErrorCode.BANKBOOK_ACCESS_FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public void checkBankbookAccessibility(Integer bookNo, Integer userNo, HttpSession session) {
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        if (bankUser.isAdmin())
            return;

        checkBankbookOwner(bookNo, userNo);
    }

    @Transactional(readOnly = true)
    public void checkBankbookOwner(Integer bookNo, Integer userNo) {
        Bankbook bankbook = bankbookService.findBankbookByBookNo(bookNo);
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        if (!bankUser.isOwned(bankbook))
            throw new BankbookForbiddenException(bookNo, GlobalErrorCode.BANKBOOK_ACCESS_FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public void checkTransactionHistoryAccessibility(Long historyNo, Integer userNo, HttpSession session) {
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        if (bankUser.isAdmin())
            return;

        checkTransactionHistoryOwner(historyNo, userNo);
    }

    @Transactional(readOnly = true)
    public void checkTransactionHistoryOwner(Long historyNo, Integer userNo) {
        TransactionHistory transactionHistory = transactionHistoryService.findTransactionHistory(historyNo);
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        if (!bankUser.isOwned(transactionHistory))
            throw new TransactionHistoryForbiddenException(historyNo, GlobalErrorCode.BANKBOOK_ACCESS_FORBIDDEN);
    }
}
