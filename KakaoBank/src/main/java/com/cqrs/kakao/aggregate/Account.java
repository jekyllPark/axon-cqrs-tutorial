package com.cqrs.kakao.aggregate;

import com.cqrs.command.transfer.KakaoBankTransferCommand;
import com.cqrs.event.TransferApprovedEvent;
import com.cqrs.event.TransferDeniedEvent;
import com.cqrs.kakao.event.AccountCreationEvent;
import com.cqrs.kakao.command.AccountCreationCommand;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Entity
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @AggregateIdentifier
    @Id
    private String accountId;
    private Long balance;

    @CommandHandler
    public Account(AccountCreationCommand command) throws IllegalAccessException {
        log.debug("handling in {} {}", this.getClass().getName(), command);
        if (command.getBalance() <= 0) throw new IllegalAccessException("invalid command");
        apply(new AccountCreationEvent(command.getAccountId(), command.getBalance()));
    }

    @EventSourcingHandler
    protected void on(AccountCreationEvent event) {
        log.debug("event {} ", event);
        this.accountId = event.getAccountId();
        this.balance = event.getBalance();
    }

    @CommandHandler
    protected void on(KakaoBankTransferCommand command) throws InterruptedException {
        log.debug("handling in {} {}", this.getClass().getName(), command);
        if (this.balance < command.getAmount()) {
            apply(TransferDeniedEvent.builder()
                    .srcAccountId(command.getSrcAccountId())
                    .dstAccountId(command.getDstAccountId())
                    .amount(command.getAmount())
                    .description("The balance is insufficient.")
                    .transferId(command.getTransferId())
                    .build()
            );
        } else {
            apply(TransferApprovedEvent.builder()
                    .srcAccountId(command.getSrcAccountId())
                    .dstAccountId(command.getDstAccountId())
                    .transferId(command.getTransferId())
                    .amount(command.getAmount())
                    .build()
            );
        }
    }

    @EventSourcingHandler
    protected void on(TransferApprovedEvent event) {
        log.debug("event {}", event);
        this.balance -= event.getAmount();
    }
}
