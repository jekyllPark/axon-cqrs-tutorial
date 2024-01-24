package com.cqrs.query.service;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QueryServiceImpl implements QueryService {
    private final Configuration configuration;
    @Override
    public void reset() {
        configuration.eventProcessingConfiguration()
                // 에그리거트에서 지정한 프로세스 그룹
                .eventProcessorByProcessingGroup("accounts", TrackingEventProcessor.class)
                .ifPresent(e -> {
                    e.shutDown();
                    // 실제 토큰 초기화
                    e.resetTokens();
                    e.start();
                });
    }
}
