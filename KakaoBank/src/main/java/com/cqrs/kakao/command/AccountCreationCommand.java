package com.cqrs.kakao.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@NoArgsConstructor
@AllArgsConstructor
@ToString @Getter
public class AccountCreationCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private Long balance;
}
