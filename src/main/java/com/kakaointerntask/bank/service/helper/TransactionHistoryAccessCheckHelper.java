package com.kakaointerntask.bank.service.helper;

import com.kakaointerntask.bank.annotation.TransactionHistoryAccessCheck;
import com.kakaointerntask.bank.annotation.parser.CustomSpringExpressionLanguageParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class TransactionHistoryAccessCheckHelper {
    public static final String HISTORY_NOT_EXIST = "-1";

    public Long getHistoryNo(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        TransactionHistoryAccessCheck transactionHistoryAccessCheck = method.getAnnotation(TransactionHistoryAccessCheck.class);

        Long historyNo = null;
        if (!transactionHistoryAccessCheck.historyNo().equals(HISTORY_NOT_EXIST)) {
            historyNo = CustomSpringExpressionLanguageParser.getDynamicLongValue(
                    signature.getParameterNames(), joinPoint.getArgs(), transactionHistoryAccessCheck.historyNo());

            if (historyNo == null)
                historyNo = Long.parseLong(HISTORY_NOT_EXIST);
        }

        return historyNo;
    }
}
