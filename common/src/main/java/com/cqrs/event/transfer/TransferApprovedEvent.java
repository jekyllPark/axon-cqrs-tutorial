package com.cqrs.event.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder @ToString @Getter
public class TransferApprovedEvent {
    private String srcAccountId;
    private String dstAccountId;
    private String transferId;
    private Long amount;
}
