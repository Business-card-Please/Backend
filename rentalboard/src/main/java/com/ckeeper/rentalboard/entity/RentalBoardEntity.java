package com.ckeeper.rentalboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rentalboard")
public class RentalBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx", nullable = false, updatable = false)
    private Long idx;

    @Column(name="owner", nullable = false, length = 16)
    private String nickname;

    @Column(name="title",nullable = false,length = 30)
    private String title;

    @Column(name="lecture",nullable = false,length = 20)
    private String lecture;

    @Column(name="department",nullable = false,length = 30)
    private String department;

    @Column(name="content",nullable = false,length = 2000)
    private String content;

    @CreationTimestamp
    @Column(name="cdatetime",nullable = true)
    private LocalDateTime cdatetime;

    @UpdateTimestamp
    @Column(name="udatetime",nullable = true)
    private LocalDateTime udatetime;

    @Column(name="viewcount",nullable = true)
    private Integer viewcount = 0;
}
