package com.example.kdt11hackathon.problem.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProblemAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String answerText;

    @OneToOne
    private Problem problem;

    @Builder
    public ProblemAnswer(String answerText, Problem problem) {
        this.answerText = answerText;
        this.problem = problem;
    }
}
