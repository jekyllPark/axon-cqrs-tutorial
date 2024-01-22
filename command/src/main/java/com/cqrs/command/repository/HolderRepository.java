package com.cqrs.command.repository;

import com.cqrs.command.aggregate.HolderAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HolderRepository extends JpaRepository<HolderAggregate, String> {
    Optional<HolderAggregate> findHolderAggregateByHolderId(String id);
}
