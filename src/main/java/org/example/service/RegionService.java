package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegionService {

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode root() {
        try {
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("hudud.json");
            return mapper.readTree(is).get("areas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 1️⃣ Viloyatlar
    public void sendRegions(AbsSender bot, Long chatId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (JsonNode region : root()) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(region.get("names").get("uz").asText());
            btn.setCallbackData("region_" + region.get("code").asText());
            rows.add(List.of(btn));
        }

        send(bot, chatId, "🏙 <b>Viloyatni tanlang</b>", rows);
    }

    // 2️⃣ Tumanlar
    public void sendDistricts(AbsSender bot, Long chatId, String regionCode) {

        for (JsonNode region : root()) {
            if (region.get("code").asText().equals(regionCode)) {

                List<List<InlineKeyboardButton>> rows = new ArrayList<>();

                for (JsonNode district :
                        region.get("children").get("areas")) {

                    InlineKeyboardButton btn = new InlineKeyboardButton();
                    btn.setText(district.get("names").get("uz").asText());
                    btn.setCallbackData(
                            "district_" + regionCode + "_" +
                                    district.get("code").asText()
                    );
                    rows.add(List.of(btn));
                }

                send(bot, chatId,
                        "🏘 <b>Tumanni tanlang</b>", rows);
                return;
            }
        }
    }

    private void send(
            AbsSender bot,
            Long chatId,
            String text,
            List<List<InlineKeyboardButton>> rows
    ) {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();
        kb.setKeyboard(rows);

        SendMessage msg = new SendMessage(
                chatId.toString(), text);
        msg.setParseMode("HTML");
        msg.setReplyMarkup(kb);

        try {
            bot.execute(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
