package com.matteoveroni.wordsremember.utilities;

import java.util.Collection;

/**
 * @author Matteo Veroni
 */

public final class Str {

    public static final String concat(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        concatStrings(strings, stringBuilder);
        return stringBuilder.toString();
    }

    public static final String concat(Collection<String> strings) {
        final StringBuilder stringBuilder = new StringBuilder();
        concatStrings(strings.toArray(new String[strings.size()]), stringBuilder);
        return stringBuilder.toString();
    }

    private static final StringBuilder concatStrings(String[] strings, StringBuilder stringBuilder) {
        for (String s : strings) {
            stringBuilder.append(s);
        }
        return stringBuilder;
    }
}
