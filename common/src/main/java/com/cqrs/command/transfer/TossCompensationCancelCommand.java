package com.cqrs.command.transfer;

public class TossCompensationCancelCommand extends AbstractCompensationCancelCommand {
    @Override
    public String toString() {
        return "TossCompensationCancelCommand{" +
                "srcAccountID='" + srcAccountId + '\'' +
                ", dstAccountID='" + dstAccountId + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferId + '\'' +
                '}';
    }
}
