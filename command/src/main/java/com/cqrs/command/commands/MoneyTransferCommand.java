package com.cqrs.command.commands;

import com.cqrs.command.transfer.KakaoBankTransferCommand;
import com.cqrs.command.transfer.TossBankTransferCommand;
import com.cqrs.command.transfer.factory.TransferCommandFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.function.Function;

@AllArgsConstructor
@ToString @Getter
public class MoneyTransferCommand {
    private String srcAccountId;
    @TargetAggregateIdentifier
    private String dstAccountId;
    private Long amount;
    private String transferId;
    private BankType bankType;

    public enum BankType {
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
