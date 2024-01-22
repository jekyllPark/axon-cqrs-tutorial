package com.cqrs.command.config;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(AxonAutoConfiguration.class)
public class AxonConfig {
    @Bean
    SimpleCommandBus commandBus(TransactionManager transactionManager) {
        return SimpleCommandBus.builder().transactionManager(transactionManager).build();
    }
}
