package com.matteoveroni.wordsremember.utilities;

import com.google.gson.Gson;

/**
 * Singleton that wraps a Gson object instance
 *
 * @author Matteo Veroni
 *
 */

public class Json {
    private volatile static Gson GSON;

    private Json() {
        GSON = new Gson();
    }

    public static Gson getInstance() {
        if (GSON == null) {
            synchronized (Json.class) {
                if (GSON == null) {
                    new Json();
                }
            }
        }
        return GSON;
    }
}
