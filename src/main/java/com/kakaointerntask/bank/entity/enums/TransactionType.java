package com.kakaointerntask.bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kakaointerntask.bank.exception.common.EnumNotFoundException;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TransactionType {
    DEPOSIT("입금"),
    WITHDRAW("출금"),
    TRANSFER("이체");

    String displayName;
    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static TransactionType create(String requestValue) throws EnumNotFoundException {
        /* 입력값이 null일시 변환되는 DTO 필드값도 null이 될 수 있도록 함 */
        if (requestValue == null)
            return null;

        return Stream.of(values())
                .filter(v -> v.toString().equalsIgnoreCase(requestValue))
                .findFirst()
                .orElseThrow(() -> new EnumNotFoundException(
                        String.format("Enum %s was not found in TransactionType enum", requestValue)));
    }
}
