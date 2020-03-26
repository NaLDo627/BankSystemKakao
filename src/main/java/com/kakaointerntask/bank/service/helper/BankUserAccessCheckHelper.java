package com.kakaointerntask.bank.service.helper;

import com.kakaointerntask.bank.annotation.BankUserAccessCheck;
import com.kakaointerntask.bank.annotation.BankbookAccessCheck;
import com.kakaointerntask.bank.annotation.parser.CustomSpringExpressionLanguageParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class BankUserAccessCheckHelper {
    public static final String BANKUSER_NOT_EXIST = "-1";

    public Integer getUserNo(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BankUserAccessCheck bankUserAccessCheck = method.getAnnotation(BankUserAccessCheck.class);

        Integer userNo = null;
        if (!bankUserAccessCheck.userNo().equals(BANKUSER_NOT_EXIST)) {
            userNo = CustomSpringExpressionLanguageParser.getDynamicIntegerValue(
                    signature.getParameterNames(), joinPoint.getArgs(), bankUserAccessCheck.userNo());

            if (userNo == null)
                userNo = Integer.parseInt(BANKUSER_NOT_EXIST);
        }

        return userNo;
    }
}
