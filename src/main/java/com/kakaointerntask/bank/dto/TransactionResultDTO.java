package com.kakaointerntask.bank.dto;

import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.TransactionBookResult;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class TransactionResultDTO {

    /* Builder 패턴 사용 시 필수 인자가 누락되지 않기 위해 생성자에 lombok builder 어노테이션 적용 */
    @Builder
    public TransactionResultDTO(Integer requestUserNo,
                                Integer receivedUserNo,
                                String requestBankbookId,
                                String requestBankbookAlias,
                                String receivedBankbookId,
                                String receivedBankbookAlias,
                                Long requestBankbookPreviousBalance,
                                Long requestBankbookRemainBalance,
                                Long receivedBankbookPreviousBalance,
                                Long receivedBankbookRemainBalance,
                                Integer requestAmount,
                                Integer transactionFee,
                                Integer actualAmount,
                                TransactionType type,
                                String memo) {
        Assert.notNull(requestUserNo, "requestUserNo should not null");
        Assert.notNull(requestBankbookId, "requestBankbookId should not null");
        Assert.notNull(requestBankbookAlias, "requestBankbookAlias should not null");
        Assert.notNull(requestBankbookPreviousBalance, "requestBankbookPreviousBalance should not null");
        Assert.notNull(requestBankbookRemainBalance, "requestBankbookRemainBalance should not null");
        Assert.notNull(requestAmount, "requestAmount should not null");
        Assert.notNull(transactionFee, "transactionFee should not null");
        Assert.notNull(actualAmount, "actualAmount should not null");
        Assert.notNull(type, "type should not null");
        if (type == TransactionType.TRANSFER) {
            Assert.notNull(receivedUserNo, "receivedUserNo should not null");
            Assert.notNull(receivedBankbookId, "receivedBankbookId should not null");
            Assert.notNull(receivedBankbookAlias, "receivedBankbookAlias should not null");
            Assert.notNull(receivedBankbookPreviousBalance, "receivedBankbookPreviousBalance should not null");
            Assert.notNull(receivedBankbookRemainBalance, "receivedBankbookRemainBalance should not null");
        }

        this.requestUserNo = requestUserNo;
        this.receivedUserNo = receivedUserNo;
        this.requestBankbookId = requestBankbookId;
        this.requestBankbookAlias = requestBankbookAlias;
        this.receivedBankbookId = receivedBankbookId;
        this.receivedBankbookAlias = receivedBankbookAlias;
        this.requestBankbookPreviousBalance = requestBankbookPreviousBalance;
        this.requestBankbookRemainBalance = requestBankbookRemainBalance;
        this.receivedBankbookPreviousBalance = receivedBankbookPreviousBalance;
        this.receivedBankbookRemainBalance = receivedBankbookRemainBalance;
        this.requestAmount = requestAmount;
        this.transactionFee = transactionFee;
        this.actualAmount = actualAmount;
        this.type = type;
        this.memo = memo;
    }

    private Integer requestUserNo;

    private Integer receivedUserNo;

    private String requestBankbookId;

    private String requestBankbookAlias;

    private String receivedBankbookId;

    private String receivedBankbookAlias;

    private Long requestBankbookPreviousBalance;

    private Long requestBankbookRemainBalance;

    private Long receivedBankbookPreviousBalance;

    private Long receivedBankbookRemainBalance;

    private Integer requestAmount;

    private Integer transactionFee;

    private Integer actualAmount;

    private TransactionType type;

    private String memo;

    public TransactionHistory toHistoryEntity() {
        BankUser requestBankUser = BankUser.newInstanceWithUserNo(this.requestUserNo);
        BankUser receivedBankUser = (type == TransactionType.TRANSFER)? BankUser.newInstanceWithUserNo(
                        this.receivedUserNo
                ) : null;

        TransactionBookResult requestBookResult = new TransactionBookResult(
                null,
                this.requestBankbookId,
                this.requestBankbookAlias,
                this.requestBankbookPreviousBalance,
                this.requestBankbookRemainBalance
        );

        TransactionBookResult receivedBookResult =
                (type == TransactionType.TRANSFER)? new TransactionBookResult(
                        null,
                        this.receivedBankbookId,
                        this.receivedBankbookAlias,
                        this.receivedBankbookPreviousBalance,
                        this.receivedBankbookRemainBalance
                ) : null;

        return new TransactionHistory(
                null,
                requestBankUser,
                receivedBankUser,
                requestBookResult,
                receivedBookResult,
                this.requestAmount,
                this.transactionFee,
                this.actualAmount,
                this.type,
                this.memo,
                TimestampUtil.now()
        );
    }
}
