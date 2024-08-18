package com.ckeeper.account.repository;

import com.ckeeper.account.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<KeywordEntity,Long> {
    void deleteAllByEmail(String email);
}
