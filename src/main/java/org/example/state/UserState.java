package org.example.state;

import java.util.HashMap;
import java.util.Map;

public class UserState {

    private static final Map<Long, String> state = new HashMap<>();

    public static void set(Long chatId, String value) {
        state.put(chatId, value);
    }

    public static String get(Long chatId) {
        return state.get(chatId);
    }

    public static void clear(Long chatId) {
        state.remove(chatId);
    }
}
