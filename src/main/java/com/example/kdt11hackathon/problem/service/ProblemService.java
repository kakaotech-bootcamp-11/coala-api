package com.example.kdt11hackathon.problem.service;

import com.example.kdt11hackathon.problem.entity.Problem;
import com.example.kdt11hackathon.problem.entity.ProblemAnswer;
import com.example.kdt11hackathon.problem.repository.ProblemAnswerRepository;
import com.example.kdt11hackathon.problem.repository.ProblemDistributedLogRepository;
import com.example.kdt11hackathon.problem.repository.ProblemRepository;
import com.example.kdt11hackathon.problem.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemDistributedLogRepository problemDistributedLogRepository;
    private final ProblemAnswerRepository problemAnswerRepository;
    private final WebClient webClient;

    // 기능
    // 1. 오늘 하루 문제 랜덤 생성
    //   1. 1. 번호를 랜덤으로 생성하여 보낸다.
    //   1. 2. 히스토리에 존재하는 번호면 다시 생성한다.

    public String generateProblem(Long problemNumber) {
        Problem problem = problemRepository.findById(problemNumber).get();

        String title = problem.getTitle();
        String problemNumber1 = problem.getNumber();

        String response = "test response";

        return response;
    }

    public String generateAnswerProblem(Long problemNumber){
        Problem problem = problemRepository.findById(problemNumber).get();
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("")
                        .build())
                .bodyValue("")
                .retrieve()
                .bodyToMono(String.class)
                .toString();

        ProblemAnswer problemAnswer = ProblemAnswer.builder()
                .answerText(response)
                .problem(problem)
                .build();

        problemAnswerRepository.save(problemAnswer);
        return response;
    }



    public Long generateProblemId() {
        Long randomNumber = Long.valueOf(RandomNumberGenerator.generate());
        if (problemDistributedLogRepository.existsByProblemId(randomNumber)) {
            return generateProblemId();
        }
        return randomNumber;
    }


    // 2. 문제 디스코드를 통해서 보내기 sendDiscordMessage()


}
