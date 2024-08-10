package com.ckeeper.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="keyword")
public class KeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "keyword", nullable = false, length = 255)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    private AccountEntity accountEntity;
}
