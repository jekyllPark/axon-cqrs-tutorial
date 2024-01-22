package com.cqrs.command.service;

import com.cqrs.command.commands.AccountCreationCommand;
import com.cqrs.command.commands.DepositMoneyCommand;
import com.cqrs.command.commands.HolderCreationCommand;
import com.cqrs.command.commands.WithdrawMoneyCommand;
import com.cqrs.command.dto.AccountDto;
import com.cqrs.command.dto.DepositDto;
import com.cqrs.command.dto.HolderDto;
import com.cqrs.command.dto.WithdrawDto;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CommandGateway commandGateway;
    @Override
    public CompletableFuture<String> createHolder(HolderDto dto) {
        return commandGateway.send(
                new HolderCreationCommand(UUID.randomUUID().toString(),
                dto.getHolderName(),
                dto.getTel(),
                dto.getAddress()
        ));
    }

    @Override
    public CompletableFuture<String> createAccount(AccountDto dto) {
        return commandGateway.send(
                new AccountCreationCommand(
                        dto.getHolderId(),
                        UUID.randomUUID().toString()
                )
        );
    }

    @Override
    public CompletableFuture<String> depositMoney(DepositDto dto) {
        return commandGateway.send(
                new DepositMoneyCommand(
                        dto.getAccountId(),
                        dto.getHolderId(),
                        dto.getAmount()
                )
        );
    }

    @Override
    public CompletableFuture<String> withdrawMoney(WithdrawDto dto) {
        return commandGateway.send(
                new WithdrawMoneyCommand(
                        dto.getAccountId(),
                        dto.getHolderId(),
                        dto.getAmount()
                )
        );
    }
}
