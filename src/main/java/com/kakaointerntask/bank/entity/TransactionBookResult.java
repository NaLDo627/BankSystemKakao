package com.kakaointerntask.bank.entity;

import com.kakaointerntask.bank.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "transaction_book_result")
public class TransactionBookResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_result_no", nullable = false)
    private Long bookResultNo;

    @NotNull
    @Column(name = "transaction_book_id", nullable = false)
    private String transactionBookId;

    @NotNull
    @Column(name="transaction_book_alias", nullable = false)
    private String transactionBookAlias;

    @Column(name = "previous_balance", nullable = false)
    private long previousBalance;

    @Column(name = "remain_balance", nullable = false)
    private long remainBalance;
}
