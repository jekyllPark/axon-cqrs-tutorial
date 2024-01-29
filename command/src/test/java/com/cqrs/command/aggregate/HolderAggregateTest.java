package com.cqrs.command.aggregate;

import com.cqrs.command.commands.HolderCreationCommand;
import com.cqrs.event.HolderCreationEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class HolderAggregateTest {
    private FixtureConfiguration<HolderAggregate> fixture;
    private static final String HOLDER_ID = UUID.randomUUID().toString();
    private static final String HOLDER_NAME = "lee";
    private static final String TEL = "010-9876-5432";
    private static final String ADDRESS = "seoul";
    private static final String COMPANY = "cqrs company";

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(HolderAggregate.class);
    }
    @Test
    void whenCommandCreatedThenEventPublished() {
        fixture.givenNoPriorActivity()
                .when(new HolderCreationCommand(HOLDER_ID, HOLDER_NAME, TEL, ADDRESS, COMPANY))
                .expectEvents(new HolderCreationEvent(HOLDER_ID, HOLDER_NAME, TEL, ADDRESS, COMPANY));
    }

}