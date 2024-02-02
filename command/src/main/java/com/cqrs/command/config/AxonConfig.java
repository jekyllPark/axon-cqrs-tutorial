package com.cqrs.command.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureAfter(AxonAutoConfiguration.class)
public class AxonConfig {

    /**
    * EmbeddedEventStore -> 이벤트를 영속화
     * Implementation of the EventStore interface, using a so-called EventStorageEngine to persist the events.
     * The EventStorageEngine has a JPA, JDBC, Mongo, and In-Memory implementation.
     *
     * In short, this is the "configure your own event store" solution of Axon's EventStore.
     * If you prefer to use something with less configuration, I'd recommend using Axon Server.
     *
     * SimpleCommandBus -> 싱글 스레드 (병렬 처리 X)
     * The SimpleCommandBus is a single JVM, single-threaded implementation of the CommandBus interface.
     * Due to this, it doesn't allow any parallelism in command dispatching and/or handling.
     * Nor does it support the distribution of commands to other instances.
     *
     * AxonServerCommandBus -> 다중 스레드 (병렬 처리 O)
     * The AxonServerCommandBus is a distributed, multi-threaded implementation of the CommandBus interface,
     * using Axon Server as the communication layer. Due to this, it allows parallelism within an application.
     * It also knows how to send and retrieve commands to and from other application instances.
     * Note that it expects Axon Server to be running and reachable by your Axon Framework application.
    * */
    @Bean
    SimpleCommandBus commandBus(TransactionManager transactionManager) {
        log.info("SimpleCommandBus build start!");
        return SimpleCommandBus.builder().transactionManager(transactionManager).build();
    }
//    @Bean
//    public AggregateFactory<AccountAggregate> aggregateFactory() {
//        return new GenericAggregateFactory<>(AccountAggregate.class);
//    }
//
//    @Bean
//    public Cache cache() {
//        return new WeakReferenceCache();
//    }
//
//    @Bean
//    public Snapshotter snapshotter(EventStore eventStore, TransactionManager transactionManager) {
//        return AggregateSnapshotter.builder()
//                .eventStore(eventStore)
//                .aggregateFactories(aggregateFactory())
//                .transactionManager(transactionManager)
//                .build();
//    }
//
//    @Bean
//    public SnapshotTriggerDefinition snapshotTriggerDefinition(EventStore eventStore, TransactionManager transactionManager) {
//        final int SNAPSHOT_THRESHOLD = 5;
//        return new EventCountSnapshotTriggerDefinition(snapshotter(eventStore, transactionManager), SNAPSHOT_THRESHOLD);
//    }
//
//    @Bean
//    public Repository<AccountAggregate> accountAggregateRepository(EventStore eventStore, SnapshotTriggerDefinition snapshotTriggerDefinition, Cache cache) {
//        return CachingEventSourcingRepository.builder(AccountAggregate.class)
//                .eventStore(eventStore)
//                .snapshotTriggerDefinition(snapshotTriggerDefinition)
//                .cache(cache)
//                .build();
//    }

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream;
    }
}
