package com.example.kdt11hackathon.problem.service;

import com.example.kdt11hackathon.problem.entity.Problem;
import com.example.kdt11hackathon.problem.entity.ProblemAnswer;
import com.example.kdt11hackathon.problem.repository.ProblemAnswerRepository;
import com.example.kdt11hackathon.problem.repository.ProblemDistributedLogRepository;
import com.example.kdt11hackathon.problem.repository.ProblemRepository;
import com.example.kdt11hackathon.problem.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
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


//    public void  simpletest(){
//        Long id = generateProblemId();
//        String pro = generateProblem(id);
//        String response = generateAnswerProblem(id);
//        log.info(pro);
//        log.info(response);
//    }

    public String generateProblem(Long problemNumber) {
        Problem problem = problemRepository.findById(problemNumber).get();

        String title = problem.getTitle();
        String problemNumber1 = problem.getNumber();
        String problemLink = problem.getLink();
        String nowMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        int dayOfMonth = LocalDate.now().getDayOfMonth();

        String header = "🚨 " + nowMonth + " " + dayOfMonth + "일" + " 🚨" + "\n";


        return header + title + " " + problemNumber1 + "\n" + problemLink;
    }

    public List<String> generateAnswerProblem(Long problemNumber) {
        Problem problem = problemRepository.findById(problemNumber).get();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("title", problem.getTitle());
        requestBody.put("number", problem.getNumber());

        Map<String, List<String>> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/llm")
                        .build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        List<String> ai = response.get("output");
        ai.stream().forEach(a -> log.info(a));
//        ProblemAnswer problemAnswer = ProblemAnswer.builder()
//                .answerText(response)
//                .problem(problem)
//                .build();

//        problemAnswerRepository.save(problemAnswer);
        return ai;
    }

    public String getProblemAnswer(Long problemNumber) {
        Problem problem = problemRepository.findById(problemNumber).get();
        ProblemAnswer problemAnswer = problemAnswerRepository.findByProblem(problem).get();

        return problemAnswer.getAnswerText();
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
