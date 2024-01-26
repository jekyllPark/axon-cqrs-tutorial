package com.cqrs.toss.config;

import com.cqrs.query.loan.LoanLimitQuery;
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
                LoanLimitQuery.class
        });
        return xStream;
    }
}
