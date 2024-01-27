package com.cqrs.command.transfer;

import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString
@Getter
public abstract class AbstractTransferCommand {
    @TargetAggregateIdentifier
    protected String srcAccountId;
    protected String dstAccountId;
    protected Long amount;
    protected String transferId;

    public AbstractTransferCommand create(String srcAccountId, String dstAccountId, Long amount, String transferId) {
        this.srcAccountId = srcAccountId;
        this.dstAccountId = dstAccountId;
        this.transferId = transferId;
        this.amount = amount;
        return this;
    }
}
