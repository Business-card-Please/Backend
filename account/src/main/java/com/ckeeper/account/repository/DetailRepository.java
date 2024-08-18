package com.ckeeper.account.repository;

import com.ckeeper.account.entity.DetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepository extends JpaRepository<DetailEntity, Long> {
    boolean existsByEmail(String email);
    void deleteAllByEmail(String email);
}
