package com.cqrs.query.controller;

import com.cqrs.query.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HolderAccountController {
    private final QueryService queryService;
    @PostMapping("/reset")
    public void reset() {
        queryService.reset();
    }
}
