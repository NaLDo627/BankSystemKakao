package com.kakaointerntask.bank.dto;

import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankbookDTO {
    public static BankbookDTO from(Bankbook bankbook) {
        return new BankbookDTO(
                bankbook.getBookNo(),
                bankbook.getBankbookId(),
                Optional.ofNullable(bankbook.getOwnerUserNo()).orElseThrow(
                        () -> new ValidationException("ownerUserNo of Bankbook entity should not null"))
                        .getUserNo(),
                bankbook.getAlias(),
                bankbook.getType(),
                bankbook.getBalance(),
                bankbook.getInsertDate(),
                bankbook.getUpdateDate(),
                bankbook.getVersion()
        );
    }

    private Integer bookNo;

    @Size(max = 64, message = "Bankbook id must not exceed size over 64")
    private String bankbookId;

    @NotNull(message = "Owner User No must not be blank")
    private Integer ownerUserNo;

    @NotBlank(message = "Alias must not be blank")
    @Size(max = 32, message = "Alias must not exceed size over 32")
    private String alias;

    private BankbookType type;

    private Long balance;

    private Timestamp insertDate;

    private Timestamp updateDate;

    private Integer version;

    /* TODO toEntity 메서드 대신 생성자를 직접 사용 or Converter 클래스 사용 */
    /* TODO 통장번호 결정 로직 모듈화 */
    public Bankbook toNewEntity(BankUser bankUser) {
        return new Bankbook(
            null,
            String.valueOf(System.currentTimeMillis()),     /* 통장 번호는 생성 시점의 밀리세컨드 값 */
            bankUser,
            this.alias,
            this.type,
            0L,
            true,
            TimestampUtil.now(),
            TimestampUtil.now(),
            null);
    }

    public Bankbook toNewEntity() {
        return new Bankbook(
    null,
            String.valueOf(System.currentTimeMillis()),
            BankUser.newInstanceWithUserNo(this.ownerUserNo),
            this.alias,
            this.type,
    0L,
    true,
            TimestampUtil.now(),
            TimestampUtil.now(),
            null);
    }

    public Bankbook toEntity() {
        return new Bankbook(
            this.bookNo,
            this.bankbookId,
            BankUser.newInstanceWithUserNo(this.ownerUserNo),
            this.alias,
            this.type,
            this.balance,
    true,
            this.insertDate,
            this.updateDate,
            this.version);
    }
}
