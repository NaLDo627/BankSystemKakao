package com.kakaointerntask.bank.service;

import com.kakaointerntask.bank.dto.TransactionRequestDTO;
import com.kakaointerntask.bank.dto.TransactionResultDTO;

public interface TransactionService {
    TransactionResultDTO handleTransactionRequest(TransactionRequestDTO transactionRequestDTO);
}
