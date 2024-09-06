package com.example.kdt11hackathon.problem.repository;

import com.example.kdt11hackathon.problem.entity.Problem;
import com.example.kdt11hackathon.problem.entity.ProblemAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProblemAnswerRepository extends JpaRepository<ProblemAnswer, Long> {
    Optional<ProblemAnswer> findByProblem(Problem problem);

}

