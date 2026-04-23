package com.project.multimessenger.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.multimessenger.dto.MessageRequest;
import com.project.multimessenger.model.Platform;
import com.project.multimessenger.model.UserPlatformAccount;
import com.project.multimessenger.repository.PlatformRepository;
import com.project.multimessenger.repository.UserPlatformAccountRepository;

@Service
public class MessageService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private UserPlatformAccountRepository userPlatformAccountRepository;

    @Autowired
    private DiscordService discordService;

    public String processMessage(MessageRequest request) {
        try {
            if (request == null) {
                return "Invalid request";
            }

            if (request.getUserId() == null) {
                return "User ID is required";
            }

            if (request.getPlatform() == null || request.getPlatform().trim().isEmpty()) {
                return "Platform is required";
            }

            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return "Message is required";
            }

            String platformName = request.getPlatform().trim();

            System.out.println("Incoming platform = [" + platformName + "]");
            System.out.println("Incoming userId = [" + request.getUserId() + "]");
            System.out.println("Incoming message = [" + request.getMessage() + "]");

            Optional<Platform> platformOpt = platformRepository.findAll()
                    .stream()
                    .filter(p -> p.getPlatformName() != null
                            && p.getPlatformName().trim().equalsIgnoreCase(platformName))
                    .findFirst();

            if (platformOpt.isEmpty()) {
                return "Platform not found in database";
            }

            Long platformId = platformOpt.get().getId();

            Optional<UserPlatformAccount> accountOpt =
                    userPlatformAccountRepository.findByUserIdAndPlatformId(
                            request.getUserId(),
                            platformId
                    );

            if (accountOpt.isEmpty()) {
                return "No platform account found for this user";
            }

            String platformUserId = accountOpt.get().getPlatformUserId();

            if (platformUserId == null || platformUserId.trim().isEmpty()) {
                return "Platform user ID is missing";
            }

            if ("Telegram".equalsIgnoreCase(platformName)) {
                return sendTelegramMessage(platformUserId.trim(), request.getMessage().trim());
            } else if ("Discord".equalsIgnoreCase(platformName)) {
                return sendDiscordMessage(platformUserId.trim(), request.getMessage().trim());
            } else if ("Slack".equalsIgnoreCase(platformName)) {
                return "Slack integration not implemented yet";
            } else if ("WhatsApp".equalsIgnoreCase(platformName)) {
                return "WhatsApp integration not implemented yet";
            } else {
                return "Platform integration not implemented yet";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending message: " + e.getMessage();
        }
    }

    private String sendTelegramMessage(String chatId, String messageText) {
        try {
            if (botToken == null || botToken.trim().isEmpty()) {
                return "Telegram bot token is missing";
            }

            String encodedMessage = URLEncoder.encode(messageText, StandardCharsets.UTF_8);

            String urlString =
                    "https://api.telegram.org/bot" + botToken
                            + "/sendMessage?chat_id=" + chatId
                            + "&text=" + encodedMessage;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();

            InputStream stream = (responseCode >= 200 && responseCode < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Telegram response code: " + responseCode);
            System.out.println("Telegram response body: " + response);

            if (responseCode == 200) {
                return "Message sent successfully to Telegram";
            } else {
                return "Telegram send failed: " + response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending Telegram message: " + e.getMessage();
        }
    }

    private String sendDiscordMessage(String channelId, String messageText) {
        try {
            boolean sent = discordService.sendMessage(channelId, messageText);

            if (sent) {
                return "Message sent successfully to Discord";
            } else {
                return "Discord send failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending Discord message: " + e.getMessage();
        }
    }
}