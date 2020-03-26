package com.kakaointerntask.bank.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
public enum GlobalErrorCode {
    NO_ERROR(0, "OK"),

    /* Transaction Error */
    NOT_ENOUGH_BALANCE(1001, "통장 잔고가 충분하지 않습니다."),
    MAXIMUM_BALANCE_REACHED(1002, "통장 잔고 최대인 1조 원에 도달하였습니다."),
    MAXIMUM_CREDIT_REACHED(1003, "신용 한도가 초과되었습니다. 대출 금액을 상환 후 거래해 주세요."),
    RECEIVER_MAXIMUM_BALANCE_REACHED(1004, "수취 통장이 통장 잔고 최대인 1조 원에 도달하였습니다."),
    MAXIMUM_TRANSACTION_AMOUNT_REACHED(1005, "실거래 금액이 최대 한도인 5억 원을 초과하였습니다."),
    RECEIVER_BANKBOOK_NOT_FOUND(1006, "수취 통장을 찾을 수 없습니다."),
    RECEIVER_BANKBOOK_AND_SENDER_BANKBOOK_ARE_SAME(1007, "송금 통장과 수취 통장은 같을 수 없습니다."),
    TRANSACTION_UNAVAILABLE(1008, "현재 거래를 실행할 수 없습니다. 나중에 다시 시도해 주세요."),

    /* BankUser Error */
    BANKUSER_NOT_FOUND(1001, "사용자 정보를 찾을 수 없습니다."),
    BANKUSER_FOUND_BUT_ADMIN(1002, "이 사용자는 관리자입니다. 접근할 수 없습니다."),
    BANKUSER_MAXIMUM_CREDIT_REACHED(1003, "사용자의 신용 한도가 초과되었습니다."),
    BANKUSER_UPDATE_FORBIDDEN(1004, "사용자 정보 변경 권한이 없습니다."),

    /* Bankbook Error */
    BANKBOOK_NOT_FOUND(3001, "은행 통장을 찾을 수 없습니다."),
    BANKBOOK_ACCESS_FORBIDDEN(3002, "통장 접근이 거부되었습니다."),
    BANKBOOK_DELETE_BALANCE_NOT_0(3003, "통장 삭제시 잔고는 0원이어야 합니다."),
    BANKBOOK_MAXIMUM_CREDIT_LIMIT_REACHED(3004, "신용 대출 통장 한도가 초과되었습니다."),
    BANKBOOK_MAXIMUM_BALANCE_LIMIT_REACHED(3005, "통장 잔고 한도가 초과되었습니다."),
    BANKBOOK_NOT_ENOUGH_BALANCE(3006, "통장 잔고가 충분하지 않습니다."),
    BANKBOOK_TYPE_UNCHANGEABLE(3007, "통장 등급을 변경할 수 없습니다. 대출 잔액을 확인해 주세요."),

    /* History Error */
    HISTORY_NOT_FOUND(401, "거래 내역을 찾을 수 없습니다."),
    HISTORY_ACCESS_FORBIDDEN(4002, "거래 내역 접근이 거부되었습니다."),

    /* Unknown Error */
    UNKNOWN_ERROR(10000, "알 수 없는 에러입니다. 무슨 일 일까요..");

    Integer errorCode;
    String message;
    GlobalErrorCode(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static GlobalErrorCode ofErrorCode(Integer errorCode) {
        return Arrays.stream(GlobalErrorCode.values())
                .filter(value -> value.getErrorCode().equals(errorCode))
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }

    @JsonCreator
    public static GlobalErrorCode create(String requestValue) {
        /* 입력값이 null일시 변환되는 DTO 필드값도 null이 될 수 있도록 함 */
        if (requestValue == null)
            return null;

        return Stream.of(values())
                .filter(v -> v.toString().equalsIgnoreCase(requestValue))
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }
}
