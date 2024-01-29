package com.cqrs.command.aggregate;

import com.cqrs.command.commands.HolderCreationCommand;
import com.cqrs.event.HolderCreationEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Aggregate
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "holder")
@Table(name = "holder")
public class HolderAggregate {
    @AggregateIdentifier
    @Id
    @Column(name = "holder_id")
    @Getter
    private String holderId;
    @Column(name = "holder_name")
    private String holderName;
    private String tel;
    private String address;

    @OneToMany(mappedBy = "holder", orphanRemoval = true)
    private List<AccountAggregate> accounts = new ArrayList<>();

    public void registerAccount(AccountAggregate account) {
        if (!this.accounts.contains(account)) {
            this.accounts.add(account);
        }
    }

    public void unRegisterAccount(AccountAggregate account) {
        this.accounts.remove(account);
    }

    @CommandHandler
    public HolderAggregate(HolderCreationCommand command) {
        log.debug("handling {}", command);
        this.holderId = command.getHolderId();
        this.holderName = command.getHolderName();
        this.tel = command.getTel();
        this.address = command.getAddress();
        apply(new HolderCreationEvent(command.getHolderId(), command.getHolderName(), command.getTel(), command.getAddress(), command.getCompany()));
    }

    @EventSourcingHandler
    protected void createAccount(HolderCreationEvent event) {
        log.debug("applying {}", event);
        this.holderId = event.getHolderId();
        this.holderName = event.getHolderName();
        this.tel = event.getTel();
        this.address = event.getAddress();
    }

    @Override
    public String toString() {
        return "HolderAggregate{" +
                "holderId='" + holderId + '\'' +
                ", holderName='" + holderName + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
