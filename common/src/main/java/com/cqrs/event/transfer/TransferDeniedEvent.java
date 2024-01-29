package com.cqrs.event.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString @Builder
public class TransferDeniedEvent {
    private String srcAccountId;
    private String dstAccountId;
    private String transferId;
    private Long amount;
    private String description;
}
