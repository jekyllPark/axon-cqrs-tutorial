package com.cqrs.kakao.component;

import com.cqrs.query.loan.LoanLimitQuery;
import com.cqrs.query.loan.LoanLimitResult;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountLoanComponent {
    @QueryHandler
    private LoanLimitResult on(LoanLimitQuery query) {
        log.debug("handling in {}, {}", this.getClass().getName(), query);
        return LoanLimitResult.builder()
                .holderId(query.getHolderId())
                .balance(query.getBalance())
                .bankName("kakaoBank")
                /**
                 * 보유 잔고의 120% 까지 대출 가능
                 * */
                .loanLimit(Double.valueOf(query.getBalance() * 1.2).longValue())
                .build();
    }
}
