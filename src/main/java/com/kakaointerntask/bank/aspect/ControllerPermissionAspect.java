package com.kakaointerntask.bank.aspect;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.service.AuthorizationService;
import com.kakaointerntask.bank.service.helper.TransactionPermissionCheckHelper;
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
public class ControllerPermissionAspect {
    private final TransactionPermissionCheckHelper transactionPermissionCheckHelper;
    private final AuthorizationService authorizationService;

    @Pointcut("@annotation(com.kakaointerntask.bank.annotation.TransactionPermissionCheck)")
    public void checkTransactionPermissionAspectTarget() { }

    @Before("checkTransactionPermissionAspectTarget()")
    public void checkTransactionPermission(JoinPoint joinPoint) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        Integer requestUserNo = transactionPermissionCheckHelper.getRequestUserNo(joinPoint);
        authorizationService.checkRequestUserInSession(requestUserNo, session);

        String requestBookId = transactionPermissionCheckHelper.getRequestBookId(joinPoint);
        BankUser bankUser = (BankUser)session.getAttribute("bankUser");
        authorizationService.checkBankbookOwner(requestBookId, bankUser.getUserNo());
    }
}
