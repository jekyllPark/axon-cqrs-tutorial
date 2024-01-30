package com.cqrs.command.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@ToString @Getter
@NoArgsConstructor @AllArgsConstructor
public abstract class AbstractCancelTransferCommand {
    @TargetAggregateIdentifier
    protected String srcAccountId;
    protected String dstAccountId;
    protected Long amount;
    protected String transferId;
    public AbstractCancelTransferCommand create(String srcAccountId, String dstAccountId, Long amount, String transferId) {
        this.srcAccountId = srcAccountId;
        this.dstAccountId = dstAccountId;
        this.amount = amount;
        this.transferId = transferId;
        return this;
    }
}
