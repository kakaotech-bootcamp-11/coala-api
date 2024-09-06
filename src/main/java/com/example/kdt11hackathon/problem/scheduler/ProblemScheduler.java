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
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @PostConstruct
    public void init() throws Exception {
        jda = JDABuilder.createDefault(botToken).build().awaitReady();
    }

    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(fixedRate = 60 * 1000)
    public void scheduledTask() {
        Long problemId = problemService.generateProblemId();
        log.info(problemId.toString());
        MessageEmbed problem = problemService.generateProblem(problemId);

        List<Guild> guilds = jda.getGuilds();
        // 문제 보내기
        for (Guild guild : guilds) {
            for (TextChannel channel: guild.getTextChannels()) {
                channel.sendMessageEmbeds(problem).queue();
            }

        }
        List<String> response = problemService.generateAnswerProblem(problemId);
        CompletableFuture.delayedExecutor(1, TimeUnit.HOURS).execute(() -> {
            guilds.forEach(guild -> {
                        for (String message : response) {
                            sendMessageToFirstAvailableChannel(guild, message);
                        }
                    }
            );
        });
    }


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

}
