package com.cqrs.query.config;

import com.cqrs.events.AccountCreationEvent;
import com.cqrs.events.DepositMoneyEvent;
import com.cqrs.events.HolderCreationEvent;
import com.cqrs.events.WithdrawMoneyEvent;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AxonConfig {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.allowTypes(new Class[]{
                HolderCreationEvent.class,
                AccountCreationEvent.class,
                DepositMoneyEvent.class,
                WithdrawMoneyEvent.class
        });
        return xStream;
    }

}
