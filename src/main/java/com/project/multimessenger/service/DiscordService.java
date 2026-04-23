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

    @Value("${DISCORD_BOT_TOKEN}")
    private String token;

    @PostConstruct
    public void init() {
        try {
            System.out.println("Starting Discord Bot...");

            if (token == null || token.trim().isEmpty()) {
                System.out.println("Discord token missing");
                return;
            }

            jda = JDABuilder.createDefault(token).build().awaitReady();

            System.out.println("Discord Bot Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Discord startup failed:");
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String channelId, String message) {
        try {
            if (jda == null) {
                System.out.println("JDA not initialized");
                return false;
            }

            TextChannel channel = jda.getTextChannelById(channelId);

            if (channel == null) {
                System.out.println("Channel not found: " + channelId);
                return false;
            }

            channel.sendMessage(message).queue();

            return true;

        } catch (Exception e) {
            System.out.println("Discord send failed:");
            e.printStackTrace();
            return false;
        }
    }
}