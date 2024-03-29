package com.cqrs.query.service;

import com.cqrs.query.entity.HolderAccountSummary;
import com.cqrs.query.loan.LoanLimitResult;
import reactor.core.publisher.Flux;

import java.util.List;

public interface QueryService {
    void reset();
    HolderAccountSummary getAccountInfo(String holderId);
    Flux<HolderAccountSummary> getAccountInfoBySubscription(String holderId);
    List<LoanLimitResult> getAccountInfoByScatterGather(String holderId);
}
