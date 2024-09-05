package com.example.kdt11hackathon.problem.repository;

import com.example.kdt11hackathon.problem.entity.ProblemDistributedLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemDistributedLogRepository extends JpaRepository<ProblemDistributedLog, Long> {
    boolean existsByProblemId(Long problemId);
}
