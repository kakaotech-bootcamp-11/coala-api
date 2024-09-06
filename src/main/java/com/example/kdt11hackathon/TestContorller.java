package com.example.kdt11hackathon;

import com.example.kdt11hackathon.discord.DiscordService;
import com.example.kdt11hackathon.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class TestContorller {
    @Value("${discord.bot.token}")
    private String botToken;
    @Value("${discord.bot.icon}")
    private String iconURL;
    private final DiscordService discordService;
    private final ProblemService problemService;

    @GetMapping("/send")
    public void test() throws InterruptedException {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("오늘의 문제");
        eb.setDescription("testing");
        eb.addField("제목", "testing", true);
        eb.addField("문제번호", "testing", true);
        eb.setColor(Color.green);
        eb.setAuthor("코알라 봇", null, iconURL);
        eb.setThumbnail(iconURL);

        JDA jda = JDABuilder.createDefault(botToken).build().awaitReady();
        List<Guild> guilds = jda.getGuilds();
        discordService.sendIntroduceProblemMessage(guilds, eb.build());
    }

    @GetMapping("/discord/test")
    public void diecordbotTest() throws InterruptedException {
        Long problemId = problemService.generateProblemId();
        MessageEmbed problem = problemService.generateProblem(problemId);
        JDA jda = JDABuilder.createDefault(botToken).build().awaitReady();
        List<Guild> guilds = jda.getGuilds();
        // 문제 보내기
        discordService.sendIntroduceProblemMessage(guilds, problem);
        List<String> response = problemService.generateAnswerProblem(problemId);
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            guilds.forEach(guild -> {
                        for (String message : response) {
                            discordService.sendMessageToFirstAvailableChannel(guild, message);
                        }
                    }
            );
        });

    }
}
