package com.kakaointerntask.bank.aspect;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.service.AuthorizationService;
import com.kakaointerntask.bank.service.helper.BankUserAccessCheckHelper;
import com.kakaointerntask.bank.service.helper.BankbookAccessCheckHelper;
import com.kakaointerntask.bank.service.helper.TransactionHistoryAccessCheckHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ControllerAccessibleAspect {
    private final BankUserAccessCheckHelper bankUserAccessCheckHelper;
    private final BankbookAccessCheckHelper bankbookAccessCheckHelper;
    private final TransactionHistoryAccessCheckHelper transactionHistoryAccessCheckHelper;
    private final AuthorizationService authorizationService;

    @Pointcut("@annotation(com.kakaointerntask.bank.annotation.BankUserAccessCheck)")
    public void BankUserAccessAspectCheckTarget() { }

    @Before("BankUserAccessAspectCheckTarget()")
    public void checkBankUserAccessibility(JoinPoint joinPoint) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        Integer userNo = bankUserAccessCheckHelper.getUserNo(joinPoint);
        authorizationService.checkBankUserAccessibility(userNo, session);
    }

    @Pointcut("@annotation(com.kakaointerntask.bank.annotation.BankbookAccessCheck)")
    public void BankbookAccessAspectCheckTarget() { }

    @Before("BankbookAccessAspectCheckTarget()")
    public void checkBankbookAccessibility(JoinPoint joinPoint) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        Integer bookNo = bankbookAccessCheckHelper.getBankbookNo(joinPoint);
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        authorizationService.checkBankbookAccessibility(bookNo, bankUser.getUserNo(), session);
    }

    @Pointcut("@annotation(com.kakaointerntask.bank.annotation.TransactionHistoryAccessCheck)")
    public void TransactionHistoryAccessCheckAspectTarget() { }

    @Before("TransactionHistoryAccessCheckAspectTarget()")
    public void checkTransactionHistoryAccessibility(JoinPoint joinPoint) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        Long historyNo = transactionHistoryAccessCheckHelper.getHistoryNo(joinPoint);
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        authorizationService.checkTransactionHistoryAccessibility(historyNo, bankUser.getUserNo(), session);
    }
}
