package com.cqrs.command.controller;

import com.cqrs.command.dto.AccountDto;
import com.cqrs.command.dto.DepositDto;
import com.cqrs.command.dto.HolderDto;
import com.cqrs.command.dto.WithdrawDto;
import com.cqrs.command.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/holder")
    public CompletableFuture<String> createHolder(@RequestBody HolderDto req){
        return transactionService.createHolder(req);
    }

    @PostMapping("/account")
    public CompletableFuture<String> createAccount(@RequestBody AccountDto req){
        return transactionService.createAccount(req);
    }

    @PostMapping("/deposit")
    public CompletableFuture<String> deposit(@RequestBody DepositDto req){
        return transactionService.depositMoney(req);
    }

    @PostMapping("/withdrawal")
    public CompletableFuture<String> withdraw(@RequestBody WithdrawDto req){
        return transactionService.withdrawMoney(req);
    }
}
