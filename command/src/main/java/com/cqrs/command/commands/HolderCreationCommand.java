package com.cqrs.command.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@ToString
@Getter
public class HolderCreationCommand {
    @TargetAggregateIdentifier
    private String holderId;
    private String holderName;
    private String tel;
    private String address;
    private String company;
}
