package com.cqrs.query.service;

import com.cqrs.query.entity.HolderAccountSummary;
import com.cqrs.query.loan.LoanLimitQuery;
import com.cqrs.query.loan.LoanLimitResult;
import com.cqrs.query.query.AccountQuery;
import com.cqrs.query.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class QueryServiceImpl implements QueryService {
    private final Configuration configuration;
    private final QueryGateway queryGateway;
    private final AccountRepository accountRepository;
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

    @Override
    public HolderAccountSummary getAccountInfo(String holderId) {
        AccountQuery accountQuery = new AccountQuery(holderId);
        log.debug("handling {} ", accountQuery);
        return queryGateway.query(accountQuery, ResponseTypes.instanceOf(HolderAccountSummary.class)).join();
    }

    @Override
    public Flux<HolderAccountSummary> getAccountInfoBySubscription(String holderId) {
        AccountQuery accountQuery = new AccountQuery(holderId);
        log.debug("handling in subscription > {}", accountQuery);

        SubscriptionQueryResult<HolderAccountSummary, HolderAccountSummary> result = queryGateway.subscriptionQuery(
                accountQuery,
                ResponseTypes.instanceOf(HolderAccountSummary.class),
                ResponseTypes.instanceOf(HolderAccountSummary.class)
        );

        return Flux.create(
                emitter -> {
                    result.initialResult().subscribe(emitter::next);
                    result.updates()
                            .doOnNext(holder -> {
                                log.debug("do on next : {}, isCanceled : {}", holder, emitter.isCancelled());
                                if (emitter.isCancelled()) {
                                    result.close();
                                }
                            })
                            .doOnComplete(emitter::complete)
                            .subscribe(emitter::next);
                }
        );
    }
    @Override
    public List<LoanLimitResult> getAccountInfoByScatterGather(String holderId) {
        HolderAccountSummary summary = accountRepository.findByHolderId(holderId).orElseThrow();
        return queryGateway.scatterGather(new LoanLimitQuery(summary.getHolderId(), summary.getTotalBalance()),
                        ResponseTypes.instanceOf(LoanLimitResult.class),
                        30, TimeUnit.SECONDS)
                .collect(Collectors.toList());
    }
}
