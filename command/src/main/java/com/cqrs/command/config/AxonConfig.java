package com.cqrs.command.config;

import com.cqrs.command.aggregate.AccountAggregate;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.Repository;
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
//@AutoConfigureAfter(AxonAutoConfiguration.class)
public class AxonConfig {
    @Bean
    public AggregateFactory<AccountAggregate> aggregateFactory() {
        return new GenericAggregateFactory<>(AccountAggregate.class);
    }

    @Bean
    public Cache cache() {
        return new WeakReferenceCache();
    }
//    @Bean
//    SimpleCommandBus commandBus(TransactionManager transactionManager) {
//        log.info("SimpleCommandBus build start!");
//        return SimpleCommandBus.builder().transactionManager(transactionManager).build();
//    }

    @Bean
    public Snapshotter snapshotter(EventStore eventStore, TransactionManager transactionManager) {
        return AggregateSnapshotter.builder()
                .eventStore(eventStore)
                .aggregateFactories(aggregateFactory())
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(EventStore eventStore, TransactionManager transactionManager) {
        final int SNAPSHOT_THRESHOLD = 5;
        return new EventCountSnapshotTriggerDefinition(snapshotter(eventStore, transactionManager), SNAPSHOT_THRESHOLD);
    }

    @Bean
    public Repository<AccountAggregate> accountAggregateRepository(EventStore eventStore, SnapshotTriggerDefinition snapshotTriggerDefinition) {
        return EventSourcingRepository.builder(AccountAggregate.class)
                .eventStore(eventStore)
                .snapshotTriggerDefinition(snapshotTriggerDefinition)
                .build();
    }
}
