package com.cqrs.kakao.service;

import com.cqrs.kakao.command.AccountCreationCommand;
import com.cqrs.kakao.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CommandGateway commandGateway;
    @Override
    public String createAccount(AccountDto dto) {
        return commandGateway.sendAndWait(new AccountCreationCommand(dto.getAccountId(), dto.getBalance()));
    }
}
