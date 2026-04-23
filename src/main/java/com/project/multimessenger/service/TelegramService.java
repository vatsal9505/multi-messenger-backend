package com.project.multimessenger.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String token;

    public void sendMessage(String chatId, String message) {

        String url =
                "https://api.telegram.org/bot"
                + token
                + "/sendMessage?chat_id="
                + chatId
                + "&text="
                + message;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(url, String.class);
    }
}