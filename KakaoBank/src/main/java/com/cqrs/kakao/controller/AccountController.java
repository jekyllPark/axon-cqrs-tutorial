package com.cqrs.kakao.controller;

import com.cqrs.kakao.dto.AccountDto;
import com.cqrs.kakao.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @PostMapping("/account")
    public ResponseEntity<String> createAccount(@RequestBody AccountDto dto) {
        return ResponseEntity.ok().body(accountService.createAccount(dto));
    }
}
