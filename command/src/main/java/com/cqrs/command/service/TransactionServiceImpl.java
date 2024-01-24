package com.cqrs.command.service;

import com.cqrs.command.aggregate.HolderAggregate;
import com.cqrs.command.commands.AccountCreationCommand;
import com.cqrs.command.commands.DepositMoneyCommand;
import com.cqrs.command.commands.HolderCreationCommand;
import com.cqrs.command.commands.WithdrawMoneyCommand;
import com.cqrs.command.dto.AccountDto;
import com.cqrs.command.dto.DepositDto;
import com.cqrs.command.dto.HolderDto;
import com.cqrs.command.dto.WithdrawDto;
import com.cqrs.command.repository.HolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CommandGateway commandGateway;
    private final HolderRepository holderRepository;

    @Override
    public CompletableFuture<String> createHolder(HolderDto dto) {
        return commandGateway.send(
                new HolderCreationCommand(UUID.randomUUID().toString(),
                        dto.getHolderName(),
                        dto.getTel(),
                        dto.getAddress(),
                        dto.getCompany()
                ));
    }

    @Override
    public CompletableFuture<String> createAccount(AccountDto dto) {
        HolderAggregate holder = holderRepository.findHolderAggregateByHolderId(dto.getHolderId())
                .orElseThrow(() -> new IllegalAccessError("계정 ID가 올바르지 않습니다."));
        log.info("holder {}", holder);
        return commandGateway.send(new AccountCreationCommand(UUID.randomUUID().toString(), holder));
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
