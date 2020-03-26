package com.kakaointerntask.bank.service.helper;

import com.kakaointerntask.bank.annotation.TransactionHistoryAccessCheck;
import com.kakaointerntask.bank.annotation.TransactionPermissionCheck;
import com.kakaointerntask.bank.annotation.parser.CustomSpringExpressionLanguageParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class TransactionPermissionCheckHelper {
    public static final String BANKUSER_NOT_EXIST = "-1";

    public String getRequestBookId(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TransactionPermissionCheck transactionHistoryAccessCheck = method.getAnnotation(TransactionPermissionCheck.class);
        String requestBookId = CustomSpringExpressionLanguageParser.getDynamicStringValue(
                signature.getParameterNames(), joinPoint.getArgs(), transactionHistoryAccessCheck.requestBookId());

        if (requestBookId == null)
            requestBookId = "";

        return requestBookId;
    }

    public Integer getRequestUserNo(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TransactionPermissionCheck transactionHistoryAccessCheck = method.getAnnotation(TransactionPermissionCheck.class);

        Integer userNo = null;
        if (!transactionHistoryAccessCheck.requestUserNo().equals(BANKUSER_NOT_EXIST)) {
            userNo = CustomSpringExpressionLanguageParser.getDynamicIntegerValue(
                    signature.getParameterNames(), joinPoint.getArgs(), transactionHistoryAccessCheck.requestUserNo());

            if (userNo == null)
                userNo = Integer.parseInt(BANKUSER_NOT_EXIST);
        }

        return userNo;
    }
}
