package com.kakaointerntask.bank.repository;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.entity.enums.BankbookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankbookRepository extends JpaRepository<Bankbook, Integer> {
    Optional<Bankbook> findByBookNoAndActive(Integer bookNo, boolean isActive);
    Optional<Bankbook> findByBankbookIdAndActive(String bankbookId, boolean isActive);
    List<Bankbook> findAllByActive(boolean isActive);
    List<Bankbook> findAllByOwnerUserNoAndActive(BankUser ownerUserNo, boolean isActive);
    List<Bankbook> findAllByOwnerUserNoAndTypeAndBalanceLessThanAndActive(
            BankUser ownerUserNo, BankbookType type, Long balance, boolean isActive);
}
