package com.matteoveroni.wordsremember.utilities;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author Matteo Veroni
 */

public final class Str {
    private static volatile List<String> uniqueRandomGeneratedStrings = new ArrayList<>();

    public static String concat(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        concatStrings(strings, stringBuilder);
        return stringBuilder.toString();
    }

    public static String concat(Collection<String> strings) {
        final StringBuilder stringBuilder = new StringBuilder();
        concatStrings(strings.toArray(new String[strings.size()]), stringBuilder);
        return stringBuilder.toString();
    }

    private static StringBuilder concatStrings(String[] strings, StringBuilder stringBuilder) {
        for (String s : strings) {
            stringBuilder.append(s);
        }
        return stringBuilder;
    }

    public synchronized static String generateRandomUniqueString() {
        String str;
        do {
            str = generateRandomString();
        } while (uniqueRandomGeneratedStrings.contains(str));
        uniqueRandomGeneratedStrings.add(str);
        return str;
    }

    private static String generateRandomString() {
        Random randomGenerator = new SecureRandom();
        final int MAX_NUM_LETTERS = 30;
        final int MIN_NUM_LETTERS = 3;
        final int FIRST_ASCII_CHAR_CODE = 97;
        final int ASCII_CHAR_CODE = 25;
        StringBuilder generatedString = new StringBuilder();

        // MIN_NUM_LETTERS <= numbersOfLetters <= MAX_NUM_LETTERS
        int numberOfLetters = (randomGenerator.nextInt(MAX_NUM_LETTERS - MIN_NUM_LETTERS)) + MIN_NUM_LETTERS;

        for (int letter = 0; letter < numberOfLetters; letter++) {
            char randomChar = (char) (FIRST_ASCII_CHAR_CODE + randomGenerator.nextInt(ASCII_CHAR_CODE));
            generatedString.append(randomChar);
        }
        return generatedString.toString();
    }
}
