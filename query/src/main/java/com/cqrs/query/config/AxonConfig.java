package com.cqrs.query.config;

import com.cqrs.events.AccountCreationEvent;
import com.cqrs.events.DepositMoneyEvent;
import com.cqrs.events.HolderCreationEvent;
import com.cqrs.events.WithdrawMoneyEvent;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.async.SequentialPerAggregatePolicy;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        log.info("configure invoked");
        /**
         * Axon은 이벤트를 배치 단위로 처리하는데, 기본 배치 사이즈가 1
         * TrackingEventProcessorConfiguration 을 통해서 배치 사이즈를 조절할 수 있음.
         */
        configurer.registerTrackingEventProcessor(
                "accounts",
                org.axonframework.config.Configuration::eventStore,
                c -> TrackingEventProcessorConfiguration.forSingleThreadedProcessing().andBatchSize(100)
        );
        /**
        * 동일 에그리거트를 동일 스레드에서 처리 될 수 있게 설정
        * */
        configurer.registerSequencingPolicy("accounts",
                configuration -> SequentialPerAggregatePolicy.instance());
    }
}
