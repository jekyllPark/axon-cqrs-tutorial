package com.cqrs.command.saga;

import com.cqrs.command.commands.TransferApprovedCommand;
import com.cqrs.command.event.DepositCompletedEvent;
import com.cqrs.command.transfer.factory.TransferCommandFactory;
import com.cqrs.event.MoneyTransferEvent;
import com.cqrs.event.TransferApprovedEvent;
import com.cqrs.event.TransferDeniedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Saga
public class TransferManager {
    @Autowired
    private transient CommandGateway commandGateway;
    private TransferCommandFactory commandFactory;

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    protected void on(MoneyTransferEvent event) {
        log.debug("Created Saga instance");
        log.debug("event : {}", event);
        commandFactory = event.getCommandFactory();
        SagaLifecycle.associateWith("srcAccountId", event.getSrcAccountId());
        log.debug("Start Account Transfer");
        commandGateway.send(commandFactory.getTransferCommand());
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    protected void on(TransferApprovedEvent event) {
        log.info("이체 금액 {} / 계좌 반영 요청 {}", event.getAmount(), event);
        SagaLifecycle.associateWith("accountId", event.getDstAccountId());
        commandGateway.send(TransferApprovedCommand.builder()
                .accountId(event.getDstAccountId())
                .amount(event.getAmount())
                .transferId(event.getTransferId())
                .build()
        );
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    protected void on(TransferDeniedEvent event) {
        log.info("계좌 이체 실패 : {}", event);
        log.info("cause : {}", event.getDescription());
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "accountId")
    @EndSaga
    protected void on(DepositCompletedEvent event) {
        log.info("계좌 이체 성공 : {}", event);
    }
}
