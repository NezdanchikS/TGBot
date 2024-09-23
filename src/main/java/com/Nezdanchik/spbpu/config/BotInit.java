package com.Nezdanchik.spbpu.config;

import com.Nezdanchik.spbpu.service.TelegramMovieBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInit {
    private final TelegramMovieBot telegramMovieBot;

    @Autowired
    public BotInit(TelegramMovieBot telegramMovieBot) {
        this.telegramMovieBot = telegramMovieBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramMovieBot);
        } catch (TelegramApiException ignored) {
        }
    }
}

