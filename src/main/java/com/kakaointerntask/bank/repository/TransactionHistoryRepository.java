package com.kakaointerntask.bank.repository;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.TransactionHistory;
import com.kakaointerntask.bank.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    Integer countByRequestUserNoAndTypeAndTransactionDateBetween(BankUser bankUser, TransactionType type, Timestamp dateBefore, Timestamp dateAfter);
    List<TransactionHistory> findAllByRequestUserNoOrReceivedUserNo(BankUser requestUserNo, BankUser receivedUserNo);
}
