package com.cqrs.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString @Getter
public class AccountDto {
    private String accountId;
    private Long balance;
}
