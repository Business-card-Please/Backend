package com.ckeeper.account.repository;

import com.ckeeper.account.entity.DetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailRepository extends JpaRepository<DetailEntity, Long> {
    boolean existsByNickname(String nickname);
    void deleteAllByNickname(String nickname);
    Optional<DetailEntity> findByNickname(String nickname);
}
