package com.matteoveroni.wordsremember.utilities;

import com.google.gson.Gson;

/**
 * Singleton that wraps a unique Gson instance
 *
 * @author Matteo Veroni
 *
 */

public class Json {
    private static volatile Gson GSON_INSTANCE;

    private Json() {
    }

    public static Gson getInstance() {
        if (GSON_INSTANCE == null) {
            synchronized (Json.class) {
                if (GSON_INSTANCE == null) {
                    GSON_INSTANCE = new Gson();
                }
            }
        }
        return GSON_INSTANCE;
    }
}
