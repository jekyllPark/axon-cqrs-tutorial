package com.cqrs.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class WithdrawMoneyEvent {
    private String holderId;
    private String accountId;
    private Long amount;
}
