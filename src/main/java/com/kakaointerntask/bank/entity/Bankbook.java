package com.kakaointerntask.bank.entity;

import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.common.OperationNotPermittedException;
import com.kakaointerntask.bank.exception.entity.BankbookEntityException;
import com.kakaointerntask.bank.exception.entity.BankbookMaximumBalanceLimitException;
import com.kakaointerntask.bank.exception.entity.BankbookNotEnoughBalanceException;
import com.kakaointerntask.bank.exception.entity.BankbookTypeUnchangeableException;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Cacheable(false)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "bankbook")
public class Bankbook implements Serializable {
    public static final long MAX_BANKBOOK_BALANCE = 1000000000000L;
    private static final long serialVersionUID = 1L;

    public static Bankbook newInstanceWithBookNo(Integer bookNo) {
        return new Bankbook(bookNo);
    }

    public Bankbook(Integer bookNo) {
        this.bookNo = bookNo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_no", nullable = false)
    private Integer bookNo;

    @NotNull
    @Column(name = "bankbook_id", unique = true, nullable = false)
    private String bankbookId;

    @NotNull
    @ManyToOne
    @JoinColumn(name="owner_user_no", nullable = false)
    private BankUser ownerUserNo;

    @NotNull
    @Column(name = "alias", nullable = false)
    private String alias;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name="type", nullable = false)
    private BankbookType type;

    @Column(name="balance", nullable = false)
    @ColumnDefault("0")
    private long balance;

    @Column(name="is_active", nullable = false)
    @ColumnDefault("1")
    private boolean active;

    @NotNull
    @Column(name="insert_date", nullable = false)
    private Timestamp insertDate;

    @Setter
    @NotNull
    @Column(name="update_date", nullable = false)
    private Timestamp updateDate;

    @Version
    @ColumnDefault("0")
    private Integer version;

    public void active() {
        this.active = true;
        updateDateNow();
    }

    public void inactive() {
        this.active = false;
        updateDateNow();
    }

    public void addBalance(long amount) throws BankbookEntityException {
        if (balance + amount > MAX_BANKBOOK_BALANCE)
            throw new BankbookMaximumBalanceLimitException(this.bookNo, GlobalErrorCode.BANKBOOK_MAXIMUM_BALANCE_LIMIT_REACHED);

        this.balance += amount;
        updateDateNow();
    }

    public void subBalance(long amount) throws BankbookEntityException {
        if (this.balance - amount < 0L && this.type == BankbookType.NORMAL)
            throw new BankbookNotEnoughBalanceException(this.bookNo, GlobalErrorCode.BANKBOOK_NOT_ENOUGH_BALANCE);

        this.balance -= amount;
        updateDateNow();
    }

    public void updateType(BankbookType type) throws BankbookEntityException {
        Objects.requireNonNull(type);

        if (type == BankbookType.NORMAL && this.balance < 0)
            throw new BankbookTypeUnchangeableException(this.bookNo, GlobalErrorCode.BANKBOOK_TYPE_UNCHANGEABLE);

        this.type = type;
        updateDateNow();
    }

    private void updateDateNow() {
        this.updateDate = TimestampUtil.now();
    }
}
