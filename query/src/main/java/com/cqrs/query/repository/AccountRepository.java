package com.cqrs.query.repository;

import com.cqrs.query.entity.HolderAccountSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<HolderAccountSummary, String> {
    Optional<HolderAccountSummary> findByHolderId(String holderId);
}
