package com.cqrs.query.projection;

import com.cqrs.events.AccountCreationEvent;
import com.cqrs.events.DepositMoneyEvent;
import com.cqrs.events.HolderCreationEvent;
import com.cqrs.events.WithdrawMoneyEvent;
import com.cqrs.query.entity.HolderAccountSummary;
import com.cqrs.query.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.NoSuchElementException;

@Slf4j
@Component
@AllArgsConstructor
public class HolderAccountProjection {
    private final AccountRepository accountRepository;

    @EventHandler
    protected void on(HolderCreationEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary accountSummary = HolderAccountSummary.builder()
                .holderId(event.getHolderId())
                .name(event.getHolderName())
                .address(event.getAddress())
                .tel(event.getTel())
                .totalBalance(0L)
                .accountCnt(0L)
                .build();
        accountRepository.save(accountSummary);
    }

    @EventHandler
    protected void on(AccountCreationEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary holderAccount = getHolderAccountSummary(event.getHolderId());
        holderAccount.setAccountCnt(holderAccount.getAccountCnt() + 1);
        accountRepository.save(holderAccount);
    }

    @EventHandler
    protected void on(DepositMoneyEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary holderAccount = getHolderAccountSummary(event.getHolderId());
        holderAccount.setTotalBalance(holderAccount.getTotalBalance() + event.getAmount());
        accountRepository.save(holderAccount);
    }

    @EventHandler
    protected void on(WithdrawMoneyEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary holderAccount = getHolderAccountSummary(event.getHolderId());
        holderAccount.setTotalBalance(holderAccount.getTotalBalance() - event.getAmount());
        accountRepository.save(holderAccount);
    }

    private HolderAccountSummary getHolderAccountSummary(String holderId) {
        return accountRepository.findByHolderId(holderId).orElseThrow(() -> new NoSuchElementException("there is no such a holder"));
    }
}
