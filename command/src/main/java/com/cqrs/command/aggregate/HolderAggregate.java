package com.cqrs.command.aggregate;

import com.cqrs.command.commands.HolderCreationCommand;
import com.cqrs.events.HolderCreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@RequiredArgsConstructor
@Aggregate
public class HolderAggregate {
    @AggregateIdentifier
    private String holderId;
    private String holderName;
    private String tel;
    private String address;

    @CommandHandler
    public HolderAggregate(HolderCreationCommand command) {
        log.debug("handling {}", command);
        apply(new HolderCreationEvent(command.getHolderId(), command.getHolderName(), command.getTel(), command.getAddress()));
    }

    @EventSourcingHandler
    protected void createAccount(HolderCreationEvent event) {
        log.debug("applying {}", event);
        this.holderId = event.getHolderId();
        this.holderName = event.getHolderName();
        this.tel = event.getTel();
        this.address = event.getAddress();
    }
}
