package com.kakaointerntask.bank.entity;

import com.kakaointerntask.bank.entity.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "transaction_history")
public class TransactionHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_no", nullable = false)
    private Long historyNo;

    @ManyToOne
    @JoinColumn(name = "request_user_no")
    private BankUser requestUserNo;

    @ManyToOne
    @JoinColumn(name = "received_user_no")
    private BankUser receivedUserNo;

    @OneToOne
    @JoinColumn(name = "request_book_result_no")
    private TransactionBookResult requestBookResultNo;

    @OneToOne
    @JoinColumn(name = "received_book_result_no")
    private TransactionBookResult receivedBookResultNo;

    @Column(name = "request_amount", nullable = false)
    private int requestAmount;

    @Column(name = "transaction_fee", nullable = false)
    private int transactionFee;

    @Column(name = "actual_amount", nullable = false)
    private int actualAmount;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "memo")
    private String memo;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Timestamp transactionDate;
}
