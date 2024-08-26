package com.ckeeper.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "keyword")
public class KeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false)
    private Long idx;

    @Column(name = "nickname", nullable = false, length = 16)
    private String nickname;

    @Column(name = "keyword", nullable = false, length = 255)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "nickname", referencedColumnName = "nickname", insertable = false, updatable = false)
    private AccountEntity accountEntity;
}