package com.project.multimessenger.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@Service
public class DiscordService {

    private JDA jda;

    @Value("${discord.bot.token}")
    private String token;

    @PostConstruct
    public void init() {
        try {
            if (token == null || token.trim().isEmpty()) {
                System.out.println("Discord token is missing");
                return;
            }

            jda = JDABuilder.createDefault(token).build();
            jda.awaitReady();
            System.out.println("Discord connected!");
        } catch (Exception e) {
            System.out.println("Discord failed to start: " + e.getMessage());
        }
    }

    public boolean sendMessage(String channelId, String message) {
        try {
            if (jda == null) {
                System.out.println("Discord JDA is not initialized");
                return false;
            }

            TextChannel channel = jda.getTextChannelById(channelId);

            if (channel == null) {
                System.out.println("Channel not found");
                return false;
            }

            channel.sendMessage(message).complete();
            return true;

        } catch (Exception e) {
            System.out.println("Discord send failed: " + e.getMessage());
            return false;
        }
    }
}