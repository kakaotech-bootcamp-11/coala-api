package com.example.kdt11hackathon.problem.service;

import com.example.kdt11hackathon.problem.entity.Problem;
import com.example.kdt11hackathon.problem.entity.ProblemAnswer;
import com.example.kdt11hackathon.problem.repository.ProblemAnswerRepository;
import com.example.kdt11hackathon.problem.repository.ProblemDistributedLogRepository;
import com.example.kdt11hackathon.problem.repository.ProblemRepository;
import com.example.kdt11hackathon.problem.util.RandomNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.awt.*;
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
    public static final String ICON_URL = "https://cdn.discordapp.com/attachments/1257525788673179773/1280813858386087997/8cdc9d07-5582-4f65-bfd8-07978a1bbf20.jpeg?ex=66db6c9c&is=66da1b1c&hm=b0b9c868108615ae04e1e012434e1db447fd72b80447b4a693525b9aa590a49e&";
    private final ProblemRepository problemRepository;
    private final ProblemDistributedLogRepository problemDistributedLogRepository;
    private final ProblemAnswerRepository problemAnswerRepository;
    private final WebClient webClient;

    // 기능
    // 1. 오늘 하루 문제 랜덤 생성
    //   1. 1. 번호를 랜덤으로 생성하여 보낸다.
    //   1. 2. 히스토리에 존재하는 번호면 다시 생성한다.


    public void simpletest() {
        Long id = generateProblemId();
        MessageEmbed pro = generateProblem(id);
        List<String> response = generateAnswerProblem(id);
        log.info(pro.toString());
        response.forEach(a -> log.info(a));
    }

    public MessageEmbed generateProblem(Long problemNumber) {
        Problem problem = problemRepository.findById(problemNumber).get();

        String title = problem.getTitle();
        String problemNumber1 = problem.getNumber();
        String problemLink = problem.getLink();
        String nowMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        int dayOfMonth = LocalDate.now().getDayOfMonth();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("오늘의 문제", problemLink);
        eb.setDescription("🚨 " + nowMonth + " " + dayOfMonth + "일" + " 🚨" + "\n");
        eb.addField("제목", title, true);
        eb.addField("문제번호", problemNumber1, true);
        eb.setColor(Color.green);
        eb.setAuthor("코알라 봇", null, ICON_URL);
        eb.setThumbnail(ICON_URL);

        return eb.build();
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
