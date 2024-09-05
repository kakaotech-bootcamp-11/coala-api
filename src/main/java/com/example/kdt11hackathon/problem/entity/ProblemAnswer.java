package com.example.kdt11hackathon.problem.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ProblemAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String answerText;

    @OneToOne
    private Problem problem;

    @Builder
    public ProblemAnswer(String answerText, Problem problem) {
        this.answerText = answerText;
        this.problem = problem;
    }
}
