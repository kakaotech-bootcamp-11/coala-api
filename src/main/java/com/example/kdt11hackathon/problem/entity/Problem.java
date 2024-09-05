package com.example.kdt11hackathon.problem.entity;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 20)
    private String number;

    private String link;

    private AlgorithmType type;

    private Tier tier;

    @Builder
    public Problem(String title, String number, String link, AlgorithmType type, Tier tier) {
        this.title = title;
        this.number = number;
        this.link = link;
        this.type = type;
        this.tier = tier;
    }
}
