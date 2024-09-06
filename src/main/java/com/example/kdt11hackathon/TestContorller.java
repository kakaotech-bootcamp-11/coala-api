package com.example.kdt11hackathon;

import com.example.kdt11hackathon.discord.DiscordService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestContorller {
    @Value("${discord.bot.token}")
    private String botToken;
    @Value("${discord.bot.icon}")
    private String iconURL;
    private final DiscordService discordService;

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
}
