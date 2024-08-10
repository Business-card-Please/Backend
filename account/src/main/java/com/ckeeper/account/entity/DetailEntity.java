package com.ckeeper.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;

@Getter
@Setter
@Entity
@Table(name = "detail")
public class DetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false)
    private Long idx;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "nickname", nullable = false, length = 12)
    private String nickname;

    @Column(name = "grade", nullable = false)
    private Short grade;

    @Column(name = "department_1",nullable = false,length = 30)
    private String department1;

    @Column(name = "department_2",length = 30)
    private String department2;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    private AccountEntity accountEntity;
}
