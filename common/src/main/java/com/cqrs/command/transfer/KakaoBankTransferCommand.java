package com.cqrs.command.transfer;

public class KakaoBankTransferCommand extends AbstractTransferCommand{
    @Override
    public String toString() {
        return "KakaoBankTransferCommand{" +
                "srcAccountID='" + srcAccountId + '\'' +
                ", dstAccountID='" + dstAccountId + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferId + '\'' +
                '}';
    }
}
