package com.cqrs.event;

import com.cqrs.command.transfer.factory.TransferCommandFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class MoneyTransferEvent {
    private String dstAccountId;
    private String srcAccountId;
    private Long amount;
    private String transferId;
    private TransferCommandFactory commandFactory;
}
