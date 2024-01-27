package com.cqrs.command.transfer;

import lombok.ToString;

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
