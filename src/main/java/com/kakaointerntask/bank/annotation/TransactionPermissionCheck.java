package com.kakaointerntask.bank.annotation;

import com.kakaointerntask.bank.dto.TransactionRequestDTO;
import com.kakaointerntask.bank.service.helper.TransactionPermissionCheckHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* Controller 에서 파라미터가 DTO가 포함된 메서드에 사용하면 AOP에서 권한 체크 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TransactionPermissionCheck {
    String requestBookId() default "";
    String requestUserNo() default TransactionPermissionCheckHelper.BANKUSER_NOT_EXIST;
}
