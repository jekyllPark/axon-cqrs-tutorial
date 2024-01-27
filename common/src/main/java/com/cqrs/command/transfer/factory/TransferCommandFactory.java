package com.cqrs.command.transfer.factory;

import com.cqrs.command.transfer.AbstractTransferCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransferCommandFactory {
    private final AbstractTransferCommand transferCommand;
    public void create(String srcAccountId, String dstAccountId, Long amount, String transferId) {
        transferCommand.create(srcAccountId, dstAccountId, amount, transferId);
    }
    public AbstractTransferCommand getTransferCommand() {
        return this.transferCommand;
    }
}
