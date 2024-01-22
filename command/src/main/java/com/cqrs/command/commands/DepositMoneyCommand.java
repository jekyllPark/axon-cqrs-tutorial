package com.cqrs.command.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@ToString
@Getter
public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private String holderId;
    private Long amount;
}
