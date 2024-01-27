package com.cqrs.kakao.service;

import com.cqrs.kakao.dto.AccountDto;

public interface AccountService {
    String createAccount(AccountDto dto);
}
