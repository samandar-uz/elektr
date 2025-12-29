package org.example.service;

import org.example.state.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class TelegramService {

    private final RegionService regionService =
            new RegionService();

    public void handle(Update update, AbsSender bot) {

        // /start
        if (update.hasMessage() && update.getMessage().hasText()) {

            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if ("/start".equals(text)) {
                regionService.sendRegions(bot, chatId);
            }

            // 3️⃣ Abonent raqami kiritildi
            if (UserState.get(chatId) != null &&
                    UserState.get(chatId).startsWith("WAIT_NUMBER")) {

                send(bot, chatId,
                        "✅ Qabul qilindi: " + text +
                                "\n(endi API ulaymiz)");
                UserState.clear(chatId);
            }
        }

        // CALLBACK
        if (update.hasCallbackQuery()) {

            String data = update.getCallbackQuery().getData();
            Long chatId =
                    update.getCallbackQuery().getMessage().getChatId();

            // Viloyat bosildi
            if (data.startsWith("region_")) {
                String regionCode =
                        data.replace("region_", "");
                regionService.sendDistricts(
                        bot, chatId, regionCode);
            }

            // Tuman bosildi
            if (data.startsWith("district_")) {
                UserState.set(chatId, "WAIT_NUMBER");

                send(bot, chatId,
                        "✍️ <b>Abonent raqamini kiriting</b>\n\n"
                                + "Masalan: <code>0208040</code>");
            }
        }
    }

    private void send(AbsSender bot, Long chatId, String text) {
        try {
            SendMessage msg =
                    new SendMessage(chatId.toString(), text);
            msg.setParseMode("HTML");
            bot.execute(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
