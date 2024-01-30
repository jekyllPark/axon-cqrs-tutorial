package com.cqrs.command.transfer.factory;

import com.cqrs.command.transfer.AbstractCancelTransferCommand;
import com.cqrs.command.transfer.AbstractCompensationCancelCommand;
import com.cqrs.command.transfer.AbstractTransferCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransferCommandFactory {
    private final AbstractTransferCommand transferCommand;
    private final AbstractCancelTransferCommand cancelCommand;
    private final AbstractCompensationCancelCommand compensationCancelCommand;
    public void create(String srcAccountId, String dstAccountId, Long amount, String transferId) {
        transferCommand.create(srcAccountId, dstAccountId, amount, transferId);
        cancelCommand.create(srcAccountId, dstAccountId, amount, transferId);
        compensationCancelCommand.create(srcAccountId, dstAccountId, amount, transferId);
    }
    public AbstractTransferCommand getTransferCommand() {
        return this.transferCommand;
    }

    public AbstractCancelTransferCommand getCancelCommand() {
        return this.cancelCommand;
    }

    public AbstractCompensationCancelCommand getCompensationCancelCommand() {
        return this.compensationCancelCommand;
    }
}
