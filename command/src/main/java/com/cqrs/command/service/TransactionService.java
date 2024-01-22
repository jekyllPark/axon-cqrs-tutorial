package com.cqrs.command.service;

import com.cqrs.command.dto.AccountDto;
import com.cqrs.command.dto.DepositDto;
import com.cqrs.command.dto.HolderDto;
import com.cqrs.command.dto.WithdrawDto;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    CompletableFuture<String> createHolder(HolderDto dto);
    CompletableFuture<String> createAccount(AccountDto dto);
    CompletableFuture<String> depositMoney(DepositDto dto);
    CompletableFuture<String> withdrawMoney(WithdrawDto dto);
}
