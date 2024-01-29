package com.cqrs.kakao.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString @Getter
@RequiredArgsConstructor
public class AccountCreationEvent {
    private final String accountId;
    private final Long balance;
}
