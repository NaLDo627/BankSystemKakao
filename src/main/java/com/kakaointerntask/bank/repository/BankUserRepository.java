package com.kakaointerntask.bank.repository;

import com.kakaointerntask.bank.entity.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, Integer> {
    boolean existsById(String id);
    Optional<BankUser> findByUserNoAndActive(Integer userNo, boolean isActive);
    Optional<BankUser> findBankUserByIdAndActive(String id, boolean isActive);
    List<BankUser> findAllByIdNotAndAdminAndActive(String id, boolean isAdmin, boolean isActive);
}
