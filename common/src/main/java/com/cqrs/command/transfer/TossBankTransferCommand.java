package com.cqrs.command.transfer;

import lombok.ToString;

public class TossBankTransferCommand extends AbstractTransferCommand{
    @Override
    public String toString() {
        return "TossBankTransferCommand{" +
                "srcAccountID='" + srcAccountId + '\'' +
                ", dstAccountID='" + dstAccountId + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferId + '\'' +
                '}';
    }
}
