package com.ckeeper.account.repository;

import com.ckeeper.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<AccountEntity, Long> {

}
