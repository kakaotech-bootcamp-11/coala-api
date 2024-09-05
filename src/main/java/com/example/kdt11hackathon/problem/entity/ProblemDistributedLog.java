package com.example.kdt11hackathon.problem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "problem_distributed_log")
public class ProblemDistributedLog {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime created_at;

    @Column(name="problem_id")
    private Long problemId;

    @Builder
    public ProblemDistributedLog(Long problemId) {
        this.problemId = problemId;
    }
}