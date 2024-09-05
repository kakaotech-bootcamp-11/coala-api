package com.example.kdt11hackathon.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DiscordMessageServiceImpl implements DiscordMessageService {

    @Override
    public boolean sendDiscordWebHook(WebHookRequest requeset) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<DiscordWebhookMessage> entity = new HttpEntity<>(requeset.toDiscordWebhookMessage(), headers);
        log.info(entity.getBody().toString());
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    requeset.getWebHookUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
