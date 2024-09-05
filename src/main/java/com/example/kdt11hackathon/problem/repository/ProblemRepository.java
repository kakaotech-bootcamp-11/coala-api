package com.example.kdt11hackathon.problem.repository;

import com.example.kdt11hackathon.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
