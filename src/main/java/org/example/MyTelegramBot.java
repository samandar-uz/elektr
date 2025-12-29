package org.example;

import org.example.service.TelegramService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MyTelegramBot extends TelegramLongPollingBot {

    private final TelegramService telegramService;


    public MyTelegramBot(TelegramService telegramService) {
        this.telegramService = telegramService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        telegramService.handle(update, this);
    }

    @Override
    public String getBotUsername() {
        return "ELEKTRUZ_BOT";
    }

    @Override
    public String getBotToken() {
        return "7994265553:AAE1bjq6KrfVN0W_dC6fQ5RAwJaNJmhRL3c";
    }
}
