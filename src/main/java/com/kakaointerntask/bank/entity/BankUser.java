package com.kakaointerntask.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.enums.BankUserRank;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.entity.BankUserEntityException;
import com.kakaointerntask.bank.exception.entity.BankUserUpdateForbiddenException;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class BankUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static BankUser newInstanceWithUserNo(Integer userNo) {
        return new BankUser(userNo);
    }

    public BankUser(Integer userNo) {
        this.userNo = userNo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no", nullable = false)
    private Integer userNo;

    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_rank", nullable = false)
    private BankUserRank rank;

    @Column(name = "is_admin", nullable = false)
    @ColumnDefault("0")
    private boolean admin;

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

    @JsonIgnore
    @OneToMany(mappedBy = "ownerUserNo")
    private List<Bankbook> ownedBankbooks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "requestUserNo")
    private List<TransactionHistory> requestedTransactionHistories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receivedUserNo")
    private List<TransactionHistory> receivedTransactionHistories = new ArrayList<>();

    // todo optimistic lock 추가

    public boolean isOwned(Bankbook bankbook) {
        return ownedBankbooks.contains(bankbook);
    }

    public boolean isOwned(TransactionHistory transactionHistory) {
        return (requestedTransactionHistories.contains(transactionHistory) || receivedTransactionHistories.contains(transactionHistory));
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public void replacePasswordEncrypted() {
        this.password = encoder.encode(this.password);
    }

    public void active() {
        this.active = true;
        updateDateNow();
    }

    public void inactive() {
        this.active = false;
        updateDateNow();
    }

    public void updateEntity(BankUser newEntity) throws BankUserEntityException {
        if (newEntity.getUserNo() == null || !newEntity.getUserNo().equals(this.userNo))
            throw new BankUserUpdateForbiddenException(this.userNo, GlobalErrorCode.BANKUSER_UPDATE_FORBIDDEN);

        if (!StringUtils.isEmpty(newEntity.getId()))
            this.id = newEntity.getId();

        if (!StringUtils.isEmpty(newEntity.getPassword()))
            this.password = encoder.encode(newEntity.getPassword());

        if (!StringUtils.isEmpty(newEntity.getName()))
            this.name = newEntity.getName();

        if (!StringUtils.isEmpty(newEntity.getEmail()))
            this.email = newEntity.getEmail();

        if (!StringUtils.isEmpty(newEntity.getPhone()))
            this.phone = newEntity.getPhone();

        this.admin = newEntity.isAdmin();
        this.active = newEntity.isActive();
        updateDateNow();
    }

    public void updateRank(BankUserRank rank) {
        Objects.requireNonNull(rank);

        this.rank = rank;
        updateDateNow();
    }

    private void updateDateNow() {
        this.updateDate = TimestampUtil.now();
    }
}
