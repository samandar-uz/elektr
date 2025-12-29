package org.example;

import org.example.service.TelegramService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotApplication {
    public static void main(String[] args) {
        try {
            TelegramService telegramService = new TelegramService();
            MyTelegramBot bot = new MyTelegramBot(telegramService);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("✅ Bot ishga tushdi...");
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
