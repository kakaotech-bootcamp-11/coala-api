package com.example.kdt11hackathon.discord;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DiscordService {
    public void sendIntroduceProblemMessage(List<Guild> guilds, MessageEmbed problem) {
        for (Guild guild : guilds) {
            for (TextChannel channel: guild.getTextChannels()) {
                if (channel.canTalk()){
                    channel.sendMessageEmbeds(problem).queue();
                    break;
                }
            }
        }
    }

    public void sendMessageToFirstAvailableChannel(Guild guild, String message) {
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
