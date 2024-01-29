package com.cqrs.event.transfer;

import com.cqrs.command.transfer.factory.TransferCommandFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class MoneyTransferEvent {
    private String dstAccountId;
    private String srcAccountId;
    private Long amount;
    private String transferId;
    private TransferCommandFactory commandFactory;
}
