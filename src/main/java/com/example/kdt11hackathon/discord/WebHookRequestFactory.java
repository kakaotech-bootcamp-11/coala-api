package com.example.kdt11hackathon.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebHookRequestFactory {
    @Value("${service-info.username}")
    private String username;
    @Value("${service-info.avatar-url}")
    private String avatarUrl;
    @Value("${discord.webhook-url}")
    private String webHookUrl;

    public WebHookRequest createWebHookRequest(String content) {
        log.info(content);
        return WebHookRequest.builder()
                .username(this.username)
                .content(content)
                .avatarUrl(this.avatarUrl)
                .webHookUrl(this.webHookUrl)
                .build();
    }
}
