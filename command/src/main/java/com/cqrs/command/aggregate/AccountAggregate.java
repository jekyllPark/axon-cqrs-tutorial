package com.cqrs.command.aggregate;

import com.cqrs.command.commands.*;
import com.cqrs.command.event.DepositCompletedEvent;
import com.cqrs.event.transfer.MoneyTransferEvent;
import com.cqrs.event.AccountCreationEvent;
import com.cqrs.event.DepositMoneyEvent;
import com.cqrs.event.WithdrawMoneyEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity(name = "account")
@Table(name = "account")
public class AccountAggregate {
    @AggregateIdentifier
    @Id
    @Column(name = "account_id")
    private String accountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holder_id", foreignKey = @ForeignKey(name = "FK_HOLDER"))
    private HolderAggregate holder;
    private Long balance;

    public void registerHolder(HolderAggregate holder) {
        if (this.holder != null) {
            this.holder.unRegisterAccount(this);
        }
        this.holder = holder;
        this.holder.registerAccount(this);
    }

    @CommandHandler
    public AccountAggregate(AccountCreationCommand command) {
        log.debug("handling {}", command);
        this.accountId = command.getAccountId();
        HolderAggregate holder = command.getHolder();
        registerHolder(holder);
        this.balance = 0L;
        apply(new AccountCreationEvent(holder.getHolderId(), command.getAccountId()));
    }

//    @EventSourcingHandler
//    protected void createAccount(AccountCreationEvent event) {
//        log.debug("applying {}", event);
//        this.accountId = event.getAccountId();
//        this.holderId = event.getHolderId();
//        this.balance = 0L;
//    }

    @CommandHandler
    protected void depositMoney(DepositMoneyCommand command) {
        log.debug("handling {}", command);
        if (command.getAmount() <= 0) throw new IllegalStateException("The amount must be greater and equal than 0");
        this.balance += command.getAmount();
        log.debug("balance {}", this.balance);
        apply(new DepositMoneyEvent(command.getHolderId(), command.getAccountId(), command.getAmount()));
    }

//    @EventSourcingHandler
//    protected void depositMoney(DepositMoneyEvent event) {
//        log.debug("applying {}", event);
//        this.balance += event.getAmount();
//    }

    @CommandHandler
    protected void withdrawMoney(WithdrawMoneyCommand command) {
        log.debug("handling {}", command);
        if (this.balance - command.getAmount() < 0) throw new IllegalStateException("The balance is insufficient");
        else if (command.getAmount() <= 0) throw new IllegalStateException("The amount must be greater and equal than 0");
        this.balance -= command.getAmount();
        log.debug("balance {}", this.balance);
        apply(new WithdrawMoneyEvent(command.getHolderId(), command.getAccountId(), command.getAmount()));
    }

//    @EventSourcingHandler
//    protected void withdrawMoney(WithdrawMoneyEvent event) {
//        log.debug("applying {}", event);
//        this.balance -= event.getAmount();
//    }

    @CommandHandler
    protected void transferMoney(MoneyTransferCommand command) {
        log.debug("handling {}", command);
        apply(new MoneyTransferEvent(
                command.getDstAccountId(),
                command.getSrcAccountId(),
                command.getAmount(),
                command.getTransferId(),
                command.getBankType().getCommandFactory(command)
                )
        );
    }

    @EventSourcingHandler
    protected void transferMoney(MoneyTransferEvent event) {
        log.debug("applying {}", event);
    }

    @CommandHandler
    protected void transferMoney(TransferApprovedCommand command) {
        log.debug("handling {}", command);
        apply(new DepositMoneyEvent(this.holder.getHolderId(), command.getAccountId(), command.getAmount()));
        apply(new DepositCompletedEvent(command.getAccountId(), command.getTransferId()));
    }
}
