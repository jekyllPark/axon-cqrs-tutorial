package com.cqrs.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.cqrs.command.commands.MoneyTransferCommand.BankType;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {
    private String srcAccountId;
    private String dstAccountId;
    private Long amount;
    private BankType bankType;
}
