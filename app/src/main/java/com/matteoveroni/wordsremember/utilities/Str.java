package com.matteoveroni.wordsremember.utilities;

/**
 * @author Matteo Veroni
 */

public final class Str {

    public static final String concat(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
