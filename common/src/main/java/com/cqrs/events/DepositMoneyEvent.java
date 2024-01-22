package com.cqrs.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class DepositMoneyEvent {
    private String holderId;
    private String accountId;
    private Long amount;
}
