package com.kakaointerntask.bank.dto;

import com.kakaointerntask.bank.entity.TransactionBookResult;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class TransactionHistoryDTO {

    // @TODO Converter 사용하여 Entity를 DTO로 변환
    public static TransactionHistoryDTO fromRequestLogEntity(@Valid TransactionHistory transactionHistory) {
        Objects.requireNonNull(transactionHistory.getRequestBookResultNo(), "requestBookResultNo in TransactionLog should not be null");
        Objects.requireNonNull(transactionHistory.getRequestUserNo(), "requestUserNo in TransactionLog should not be null");

        TransactionBookResult requestBookResultNo = transactionHistory.getRequestBookResultNo();
        String receiverBookId = null;
        String receiverUserName = null;
        if (transactionHistory.getType() == TransactionType.TRANSFER) {
            Objects.requireNonNull(transactionHistory.getReceivedBookResultNo(), "receivedBookResultNo in TransactionLog should not be null");
            Objects.requireNonNull(transactionHistory.getReceivedUserNo(), "receivedUserNo in TransactionLog should not be null");
            receiverBookId = transactionHistory.getReceivedBookResultNo().getTransactionBookId();
            receiverUserName = transactionHistory.getReceivedUserNo().getName();
        }

        return new TransactionHistoryDTO(
                transactionHistory.getHistoryNo(),
                transactionHistory.getRequestUserNo().getName(),
                requestBookResultNo.getTransactionBookId(),
                requestBookResultNo.getTransactionBookAlias(),
                receiverUserName,
                receiverBookId,
                transactionHistory.getType(),
                transactionHistory.getRequestAmount(),
                transactionHistory.getTransactionFee(),
                transactionHistory.getActualAmount(),
                requestBookResultNo.getPreviousBalance(),
                requestBookResultNo.getRemainBalance(),
                transactionHistory.getMemo(),
                transactionHistory.getTransactionDate()
        );
    }

    public static TransactionHistoryDTO fromReceivedLogEntity(@Valid TransactionHistory transactionHistory) {
        Objects.requireNonNull(transactionHistory.getRequestBookResultNo(), "requestBookResultNo in TransactionLog should not be null");
        Objects.requireNonNull(transactionHistory.getRequestUserNo(), "requestUserNo in TransactionLog should not be null");
        Objects.requireNonNull(transactionHistory.getReceivedBookResultNo(), "receivedBookResultNo in TransactionLog should not be null");
        Objects.requireNonNull(transactionHistory.getReceivedUserNo(), "receivedUserNo in TransactionLog should not be null");

        TransactionBookResult requestBookResultNo = transactionHistory.getRequestBookResultNo();
        TransactionBookResult receivedBookResultNo = transactionHistory.getReceivedBookResultNo();
        return new TransactionHistoryDTO(
                transactionHistory.getHistoryNo(),
                transactionHistory.getRequestUserNo().getName(),
                requestBookResultNo.getTransactionBookId(),
                receivedBookResultNo.getTransactionBookAlias(),
                transactionHistory.getReceivedUserNo().getName(),
                receivedBookResultNo.getTransactionBookId(),
                transactionHistory.getType(),
                transactionHistory.getRequestAmount(),
                transactionHistory.getTransactionFee(),
                transactionHistory.getActualAmount(),
                receivedBookResultNo.getPreviousBalance(),
                receivedBookResultNo.getRemainBalance(),
                transactionHistory.getMemo(),
                transactionHistory.getTransactionDate()
        );
    }

    private Long historyNo;

    private String requestUserName;

    private String requestBookId;

    private String transactionBankbookAlias;

    private String receivedUserName;

    private String receiverBookId;

    private TransactionType type;

    private int requestAmount;

    private int transactionFee;

    private int actualAmount;

    private long previousBalance;

    private long remainBalance;

    private String memo;

    private Timestamp transactionDate;
}
