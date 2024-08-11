package com.ckeeper.account.repository;

import com.ckeeper.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Boolean existsByEmail(String email);
}
