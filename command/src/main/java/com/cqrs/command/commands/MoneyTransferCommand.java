package com.cqrs.command.commands;

import com.cqrs.command.transfer.KakaoBankTransferCommand;
import com.cqrs.command.transfer.TossBankTransferCommand;
import com.cqrs.command.transfer.factory.TransferCommandFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.function.Function;

@Builder @ToString @Getter
public class MoneyTransferCommand {
    @TargetAggregateIdentifier
    private String dstAccountId;
    private String srcAccountId;
    private Long amount;
    private String transferId;
    private BankType bankType;

    private enum BankType {
        KAKAO(command -> new TransferCommandFactory(new KakaoBankTransferCommand())),
        TOSS(command -> new TransferCommandFactory(new TossBankTransferCommand()));
        private Function<MoneyTransferCommand, TransferCommandFactory> expression;
        BankType(Function<MoneyTransferCommand, TransferCommandFactory> expression){ this.expression = expression;}
        public TransferCommandFactory getCommandFactory(MoneyTransferCommand command){
            TransferCommandFactory factory = this.expression.apply(command);
            factory.create(command.getSrcAccountId(), command.getDstAccountId(), command.amount, command.getTransferId());
            return factory;
        }
    }
}
