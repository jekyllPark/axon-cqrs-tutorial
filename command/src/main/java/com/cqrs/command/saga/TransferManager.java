package com.cqrs.command.saga;

import com.cqrs.command.commands.TransferApprovedCommand;
import com.cqrs.command.event.DepositCompletedEvent;
import com.cqrs.command.transfer.factory.TransferCommandFactory;
import com.cqrs.event.transfer.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Slf4j
@Saga
public class TransferManager {
    @Autowired
    private transient CommandGateway commandGateway;
    private TransferCommandFactory commandFactory;
    private boolean isExecutingCompensation = false;
    private boolean isAbortingCompensation = false;

    @StartSaga
    @SagaEventHandler(associationProperty = "transferId")
    protected void on(MoneyTransferEvent event) {
        log.debug("Created Saga instance");
        log.debug("event : {}", event);
        commandFactory = event.getCommandFactory();
        SagaLifecycle.associateWith("srcAccountId", event.getSrcAccountId());

        try {
            log.debug("Start Account Transfer");
            commandGateway.sendAndWait(commandFactory.getTransferCommand(), 10, TimeUnit.SECONDS);
        } catch (CommandExecutionException e) {
            log.error("Failed transfer process, Cancel process start");
            cancelTransfer();
        }
    }

    private void cancelTransfer() {
        isExecutingCompensation = true;
        log.info("보상 트랜잭션 요청");
        commandGateway.send(commandFactory.getCancelCommand());
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    protected void on(CompletedCancelTransferEvent event) {
        isExecutingCompensation = false;
        if (!isAbortingCompensation) {
            log.info("계좌 이체 취소 완료 : {}", event);
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    protected void on(TransferApprovedEvent event) {
        if (!isExecutingCompensation && !isAbortingCompensation) {
            log.info("이체 금액 {} / 계좌 반영 요청 {}", event.getAmount(), event);
            SagaLifecycle.associateWith("accountId", event.getDstAccountId());
            commandGateway.send(TransferApprovedCommand.builder()
                    .accountId(event.getDstAccountId())
                    .amount(event.getAmount())
                    .transferId(event.getTransferId())
                    .build()
            );
        }
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    protected void on(TransferDeniedEvent event) {
        log.info("계좌 이체 실패 : {}", event);
        log.info("cause : {}", event.getDescription());
        if (isExecutingCompensation) {
            isAbortingCompensation = true;
            log.info("보상 트랜잭션 취소 요청 : {}", event);
            commandGateway.send(commandFactory.getCompensationCancelCommand());
        } else {
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "srcAccountId")
    @EndSaga
    protected void on(CompletedCompensationCancelEvent event) {
        isAbortingCompensation = false;
        log.info("보상 트랜잭션 취소 완료 : {}", event);
    }

    @SagaEventHandler(associationProperty = "accountId")
    @EndSaga
    protected void on(DepositCompletedEvent event) {
        log.info("계좌 이체 성공 : {}", event);
    }
}
