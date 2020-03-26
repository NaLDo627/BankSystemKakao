package com.kakaointerntask.bank.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kakaointerntask.bank.exception.common.EnumNotFoundException;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum BankbookType {
    NORMAL( "일반"),
    CREDIT( "신용대출");

    String displayName;

    BankbookType( String displayName) {
        this.displayName = displayName;
    }

    public static BankbookType ofTypeCode(Integer typeCode) throws EnumNotFoundException {
        return null;
    }

    @JsonCreator
    public static BankbookType create(String requestValue) throws EnumNotFoundException {
        /* 입력값이 null일시 변환되는 DTO 필드값도 null이 될 수 있도록 함 */
        if (requestValue == null)
            return null;

        return Stream.of(values())
                .filter(v -> v.toString().equalsIgnoreCase(requestValue))
                .findFirst()
                .orElseThrow(() -> new EnumNotFoundException(
                        String.format("Enum %s was not found in BankbookType enum", requestValue)));
    }
}
