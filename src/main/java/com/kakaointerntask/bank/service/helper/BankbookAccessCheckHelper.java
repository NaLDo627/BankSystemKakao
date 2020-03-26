package com.kakaointerntask.bank.service.helper;

import com.kakaointerntask.bank.annotation.BankbookAccessCheck;
import com.kakaointerntask.bank.annotation.parser.CustomSpringExpressionLanguageParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class BankbookAccessCheckHelper {
    public static final String BANKBOOK_NOT_EXIST = "-1";

    public Integer getBankbookNo(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BankbookAccessCheck bankbookAccessCheck = method.getAnnotation(BankbookAccessCheck.class);

        Integer bookNo = null;
        if (!bankbookAccessCheck.bookNo().equals(BANKBOOK_NOT_EXIST)) {
            bookNo = CustomSpringExpressionLanguageParser.getDynamicIntegerValue(
                    signature.getParameterNames(), joinPoint.getArgs(), bankbookAccessCheck.bookNo());

            if (bookNo == null)
                bookNo = Integer.parseInt(BANKBOOK_NOT_EXIST);
        }

        return bookNo;
    }
}
