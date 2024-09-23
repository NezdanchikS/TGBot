package com.Nezdanchik.spbpu;

import com.Nezdanchik.spbpu.config.BotConfig;
import com.Nezdanchik.spbpu.service.TelegramMovieBot;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@ComponentScan("com.Nezdanchik.spbpu")
public class Main {
    public static void main(String[] args) {
        // Загрузка контекста Spring
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        // Получение экземпляра вашего бота
        TelegramMovieBot bot = context.getBean(TelegramMovieBot.class);

        // Регистрация бота вручную
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Bot successfully started!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
