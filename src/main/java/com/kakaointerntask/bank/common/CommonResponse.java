package com.kakaointerntask.bank.common;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
public class CommonResponse<T> {
    private static final CommonResponse<Void> API_OK_RESPONSE = new CommonResponse<>("OK", 0, null);

    public static CommonResponse<Void> getOkResponse() {
        return API_OK_RESPONSE;
    }

    public static CommonResponse<Void> getResponse(String message, int errorCode) {
        return new CommonResponse<>(message, errorCode, null);
    }

    public static <T> CommonResponse<T> getResponseWithData(String message, int errorCode, final T data) {
        return new CommonResponse<>(message, errorCode, data);
    }

    private String message;

    private int errorCode;

    private T data;
}
