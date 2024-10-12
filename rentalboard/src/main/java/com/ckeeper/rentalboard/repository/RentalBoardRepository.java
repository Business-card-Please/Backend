package com.ckeeper.rentalboard.repository;

import com.ckeeper.rentalboard.entity.RentalBoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RentalBoardRepository extends JpaRepository<RentalBoardEntity,Long> {
    @Query("SELECT r FROM RentalBoardEntity r WHERE r.cdatetime <= :lastDateTime AND (r.department = :department1 OR r.department = :department2) ORDER BY r.cdatetime DESC")
    Page<RentalBoardEntity> findByDateTimeTypeDefault(
            @Param("lastDateTime") LocalDateTime lastDateTime,
            @Param("department1") String department1,
            @Param("department2") String department2,
            Pageable pageable
    );

    @Query("SELECT r FROM RentalBoardEntity r WHERE r.cdatetime <= :lastDateTime AND (r.department = :department1 OR r.department = :department2) AND (r.content LIKE %:keyword%) OR (r.title LIKE %:keyword%) ORDER BY r.cdatetime DESC")
    Page<RentalBoardEntity> findByDateTimeTypeHotkeyword(
            @Param("lastDateTime") LocalDateTime lastDateTime,
            @Param("department1") String department1,
            @Param("department2") String department2,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT r FROM RentalBoardEntity r WHERE r.cdatetime <= :lastDateTime AND (r.content LIKE %:search%) OR (r.title LIKE %:search%) OR (r.lecture LIKE %:search%) OR (r.department LIKE %:search%) ORDER BY r.cdatetime DESC")
    Page<RentalBoardEntity> findByDateTimeTypeSearch(
            @Param("lastDateTime") LocalDateTime lastDateTime,
            @Param("search") String search,
            Pageable pageable
    );
}
