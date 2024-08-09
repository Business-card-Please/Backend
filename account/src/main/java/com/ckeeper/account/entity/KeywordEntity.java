package com.ckeeper.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="keyword")
public class KeywordEntity {
    @Id
    private Long idx;
}
