package com.cqrs.command.service;

import com.cqrs.command.dto.*;

import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    CompletableFuture<String> createHolder(HolderDto dto);
    CompletableFuture<String> createAccount(AccountDto dto);
    CompletableFuture<String> depositMoney(DepositDto dto);
    CompletableFuture<String> withdrawMoney(WithdrawDto dto);
    String transferMoney(TransferDto dto);
}
