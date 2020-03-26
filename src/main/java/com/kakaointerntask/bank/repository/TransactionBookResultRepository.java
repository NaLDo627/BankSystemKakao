package com.kakaointerntask.bank.repository;

import com.kakaointerntask.bank.entity.TransactionBookResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionBookResultRepository extends JpaRepository<TransactionBookResult, Long> {

}
