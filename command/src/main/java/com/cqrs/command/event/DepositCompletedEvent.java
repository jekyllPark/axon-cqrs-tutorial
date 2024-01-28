package com.cqrs.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString @Getter
@AllArgsConstructor
public class DepositCompletedEvent {
    private String accountId;
    private String transferId;
}
