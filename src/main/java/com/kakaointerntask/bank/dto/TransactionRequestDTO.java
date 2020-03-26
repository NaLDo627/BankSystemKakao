package com.kakaointerntask.bank.dto;

import com.kakaointerntask.bank.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class TransactionRequestDTO {
    @NotNull(message = "Request user number must not be blank")
    private Integer requestUserNo;

    @NotBlank(message = "Alias must not be blank")
    @Size(max = 64, message = "Transaction book id must not exceed size over 64")
    private String transactionBookId;

    private String receiverBookId;

    @NotNull
    @Range(min = 1L, message = "Amount should be over than 1")
    private Integer amount;

    @Size(max = 255, message = "Memo must not exceed size over 255")
    private String memo;
}
