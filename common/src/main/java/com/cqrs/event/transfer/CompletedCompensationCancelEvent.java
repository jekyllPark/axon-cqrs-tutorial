package com.cqrs.event.transfer;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString @Getter
public class CompletedCompensationCancelEvent {
    private String srcAccountId;
    private String dstAccountId;
    private Long amount;
    private String transferId;
}
