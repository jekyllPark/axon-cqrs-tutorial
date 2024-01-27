package com.cqrs.command.commands;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString @Getter @Builder
public class TransferApprovedCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private Long amount;
    private String transferId;
}
