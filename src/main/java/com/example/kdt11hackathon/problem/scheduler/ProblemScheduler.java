package com.example.kdt11hackathon.problem.scheduler;

import com.example.kdt11hackathon.discord.DiscordMessageService;
import com.example.kdt11hackathon.discord.WebHookRequestFactory;
import com.example.kdt11hackathon.problem.service.ProblemService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProblemScheduler {
    private final HttpMessageConverters messageConverters;
    private JDA jda;
    @Value("${discord.bot.token}")
    private String botToken;
    private final ProblemService problemService;
    private final DiscordMessageService discordMessageServiceImpl;
    private final WebHookRequestFactory webHookRequestFactory;
//    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(cron = "5 * * * * *")

    @PostConstruct
    public void init() throws Exception {
        jda = JDABuilder.createDefault(botToken).build().awaitReady();
    }


    @Scheduled(fixedRate = 10000)
    public void scheduledTask() {
        Long problemId = problemService.generateProblemId();
//        String problem = problemService.generateProblem(problemId);
//        jda.getGuilds().forEach(guild -> {
//            TextChannel channel = jda.getTextChannelById(guild.getChannels().get());
//            log.info(guild.toString());
//            if (channel != null) {
//                channel.sendMessage("test message").queue();
//                log.info("Scheduled task");
//            }else {
//                log.error("Could not find channel");
//            }
//        });


        // 문제 보내기
        String message = "test message";
        for (Guild guild : jda.getGuilds()) {
            sendMessageToFirstAvailableChannel(guild, message);
        }


//        if (channel != null) {
//            channel.sendMessage("test message").queue();
//            log.info("Scheduled task");
//        }else {
//            log.error("Could not find channel");
//        }

        // discord로 문제 정보 메시지 보내기
//        discordMessageServiceImpl.sendDiscordWebHook(
//                webHookRequestFactory.createWebHookRequest("시작점")
//                );

        // 정답 메시지 보내기

//        CompletableFuture.delayedExecutor(1, TimeUnit.HOURS).execute(() -> {
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            log.info("끝");

//            String problemAnswer = problemService.generateAnswerProblem(problemId);
//            discordMessageServiceImpl.sendDiscordWebHook(webHookRequestFactory.createWebHookRequest("테스트 끝"));
        });
    }

//    private void

    private void sendMessageToFirstAvailableChannel(Guild guild, String message) {
        // 서버의 모든 텍스트 채널을 가져와서 메시지 보낼 수 있는 첫 번째 채널 찾기
        for (TextChannel channel : guild.getTextChannels()) {
            if (channel.canTalk()) { // 봇이 해당 채널에 메시지를 보낼 수 있는지 확인
                channel.sendMessage(message).queue(
                        success -> log.info("메시지를 성공적으로 보냈습니다: {} - 서버: {} - 채널: {}", message, guild.getName(), channel.getName()),
                        error -> log.error("메시지 전송에 실패했습니다. 서버: {} - 채널: {}", guild.getName(), channel.getName(), error)
                );
                break; // 첫 번째 사용 가능한 채널에만 메시지를 보내고 루프 종료
            }
        }
    }


    private void sendDiscord(String message){
        log.info(message);
    }
}
