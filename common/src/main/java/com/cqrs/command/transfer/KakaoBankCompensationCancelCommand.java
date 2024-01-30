package com.cqrs.command.transfer;

public class KakaoBankCompensationCancelCommand extends AbstractCompensationCancelCommand {
    @Override
    public String toString() {
        return "KakaoBankCompensationCancelCommand{" +
                "srcAccountID='" + srcAccountId + '\'' +
                ", dstAccountID='" + dstAccountId + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferId + '\'' +
                '}';
    }
}
