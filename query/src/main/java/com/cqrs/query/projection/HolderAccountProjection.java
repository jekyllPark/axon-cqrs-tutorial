package com.cqrs.query.projection;

import com.cqrs.event.AccountCreationEvent;
import com.cqrs.event.DepositMoneyEvent;
import com.cqrs.event.HolderCreationEvent;
import com.cqrs.event.WithdrawMoneyEvent;
import com.cqrs.query.entity.HolderAccountSummary;
import com.cqrs.query.query.AccountQuery;
import com.cqrs.query.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.AllowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.NoSuchElementException;

@Slf4j
@Component
@EnableRetry
@AllArgsConstructor
@ProcessingGroup("accounts") // replay 대상 지정
public class HolderAccountProjection {
    private final AccountRepository accountRepository;
    private final QueryUpdateEmitter emitter;

    /**
     * 신규 read 모델이 추가되거나 기존 모델의 변경이 있을 때, eventStore에서 기존 내역을 전달 받아 재수행하기 위해 replay를 함.
     */
    @EventHandler
    @Retryable(value = {NoSuchElementException.class}, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @AllowReplay // 리플레이 적용 대상, @DisallowReplay 붙일 경우 해당 이벤트 처리 X
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
    @Retryable(value = {NoSuchElementException.class}, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @AllowReplay
    protected void on(AccountCreationEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary holderAccount = getHolderAccountSummary(event.getHolderId());
        holderAccount.setAccountCnt(holderAccount.getAccountCnt() + 1);
        accountRepository.save(holderAccount);
    }

    @EventHandler
    @Retryable(value = {NoSuchElementException.class}, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @AllowReplay
    protected void on(DepositMoneyEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary holderAccount = getHolderAccountSummary(event.getHolderId());
        holderAccount.setTotalBalance(holderAccount.getTotalBalance() + event.getAmount());

        emitter.emit(AccountQuery.class,
                query -> query.getHolderId().equals(event.getHolderId()), holderAccount
                );

        accountRepository.save(holderAccount);
    }

    @EventHandler
    @Retryable(value = {NoSuchElementException.class}, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @AllowReplay
    protected void on(WithdrawMoneyEvent event, @Timestamp Instant instant) {
        log.debug("projecting {}, timestamp : {}", event, instant.toString());
        HolderAccountSummary holderAccount = getHolderAccountSummary(event.getHolderId());
        holderAccount.setTotalBalance(holderAccount.getTotalBalance() - event.getAmount());

        emitter.emit(AccountQuery.class,
                query -> query.getHolderId().equals(event.getHolderId()), holderAccount);

        accountRepository.save(holderAccount);
    }

    private HolderAccountSummary getHolderAccountSummary(String holderId) {
        return accountRepository.findByHolderId(holderId).orElseThrow(() -> new NoSuchElementException("there is no such a holder"));
    }

    // read 모델 초기화, 리플레이 정합성을 위해 추가
    @ResetHandler
    private void resetHolderAccountInfo() {
        log.info("reset triggered");
        accountRepository.deleteAll();
    }

    @QueryHandler
    public HolderAccountSummary on(AccountQuery query) {
        log.debug("handling in holder-account-projection > {}", query);
        return accountRepository.findByHolderId(query.getHolderId()).orElseThrow(() -> new NoSuchElementException("there is no such a holder"));
    }
}
