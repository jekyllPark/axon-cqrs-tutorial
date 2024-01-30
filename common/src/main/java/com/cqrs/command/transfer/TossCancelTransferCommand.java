package com.cqrs.command.transfer;

public class TossCancelTransferCommand extends AbstractCancelTransferCommand {
    @Override
    public String toString() {
        return "TossCancelTransferCommand{" +
                "srcAccountID='" + srcAccountId + '\'' +
                ", dstAccountID='" + dstAccountId + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferId + '\'' +
                '}';
    }
}
