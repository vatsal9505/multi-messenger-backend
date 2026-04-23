package com.project.multimessenger.service;
import com.project.multimessenger.service.TelegramService;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.multimessenger.dto.MessageRequest;
import com.project.multimessenger.model.Platform;
import com.project.multimessenger.model.UserPlatformAccount;
import com.project.multimessenger.repository.PlatformRepository;
import com.project.multimessenger.repository.UserPlatformAccountRepository;

@Service
public class MessageService {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private DiscordService discordService;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private UserPlatformAccountRepository userPlatformAccountRepository;

    public String processMessage(MessageRequest request) {

        Long userId = request.getUserId();
        String message = request.getMessage();
        String platform = request.getPlatform();

        // SEND TO ALL CONNECTED PLATFORMS
        if (platform != null && platform.equalsIgnoreCase("ALL")) {

            List<UserPlatformAccount> accounts = userPlatformAccountRepository.findByUserId(userId);

            if (accounts.isEmpty()) {
                return "No connected platforms found for this user";
            }

            for (UserPlatformAccount account : accounts) {
                Long platformId = account.getPlatformId();
                String receiverId = account.getPlatformUserId();

                Optional<Platform> platformOpt = platformRepository.findById(platformId);

                if (platformOpt.isEmpty()) {
                    continue;
                }

                String platformName = platformOpt.get().getPlatformName();

                if (platformName.equalsIgnoreCase("Telegram")) {
                    telegramService.sendMessage(receiverId, message);
                } else if (platformName.equalsIgnoreCase("Discord")) {
                    discordService.sendMessage(receiverId, message);
                }
            }

            return "Message sent to all connected platforms";
        }

        // SINGLE PLATFORM SEND
        Optional<Platform> selectedPlatformOpt =
                platformRepository.findByPlatformNameIgnoreCase(platform);

        if (selectedPlatformOpt.isEmpty()) {
            return "Platform not found";
        }

        Platform selectedPlatform = selectedPlatformOpt.get();

        Optional<UserPlatformAccount> accountOpt =
                userPlatformAccountRepository.findByUserIdAndPlatformId(
                        userId,
                        selectedPlatform.getId()
                );

        if (accountOpt.isEmpty()) {
            return "No platform account found";
        }

        UserPlatformAccount account = accountOpt.get();
        String receiverId = account.getPlatformUserId();

        if (platform.equalsIgnoreCase("Telegram")) {
            telegramService.sendMessage(receiverId, message);
            return "Telegram message sent";
        }

        if (platform.equalsIgnoreCase("Discord")) {
            boolean sent = discordService.sendMessage(receiverId, message);
            return sent ? "Discord message sent" : "Discord send failed";
        }

        return "Unsupported platform";
    }
}