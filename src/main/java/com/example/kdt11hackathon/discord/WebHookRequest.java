package com.example.kdt11hackathon.discord;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
public class WebHookRequest {
    private String username;
    private String avatarUrl;
    private String content;
    private String webHookUrl;

    @Builder
    public WebHookRequest(String username, String avatarUrl, String content, String webHookUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.content = content;
        this.webHookUrl = webHookUrl;
    }

    public DiscordWebhookMessage toDiscordWebhookMessage() {
        return new DiscordWebhookMessage(this.username, this.avatarUrl, this.content);
    }
}
