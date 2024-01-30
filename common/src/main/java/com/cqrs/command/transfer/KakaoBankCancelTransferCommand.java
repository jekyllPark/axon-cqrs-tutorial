package com.cqrs.command.transfer;

public class KakaoBankCancelTransferCommand extends AbstractCancelTransferCommand {
    @Override
    public String toString() {
        return "KakaoBankCancelTransferCommand{" +
                "srcAccountID='" + srcAccountId + '\'' +
                ", dstAccountID='" + dstAccountId + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferId + '\'' +
                '}';
    }
}
