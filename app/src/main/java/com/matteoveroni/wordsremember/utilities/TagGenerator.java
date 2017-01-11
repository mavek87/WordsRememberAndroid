package com.matteoveroni.wordsremember.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

/**
 * Created by Matteo Veroni
 */

public final class TagGenerator {

    public static final String TAG = "Tag";
    public static final int MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG = 23;

    /**
     * Method for creating Android Tags for a given class. Android TAG's text length can't exceed
     * 23 characters.
     *
     * @return the corresponding TAG for class passed to the constructor
     */
    public final String generateTag(Class classInstance) {

        String tag = classInstance.getSimpleName();
        int length = tag.length();

        if (length < 1) {
            tag = generateRandomUniqueTag();
        } else {
            if (!isCapitalLetter(tag.charAt(0))) {
                 tag = capitalizeFirstLetter(tag);
            }
            if (length > MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG) {
                tag = shrinkLengthOfString(tag, MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
            }
        }
        return tag;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  Helper Methods
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Useful material:
     * <p>
     * http://stackoverflow.com/questions/20994768/how-to-reduce-length-of-uuid-generated-using-randomuuid
     * </p>
     */
    private String generateRandomUniqueTag() {
        final UUID uuid = UUID.randomUUID();
        String str_uuid = Long.toString(uuid.getLeastSignificantBits(), 94);
        return TAG + str_uuid;
    }

    private String shrinkLengthOfString(String string, int MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG) {
        final List<String> subStrings = new ArrayList<>();
        final Stack<Integer> longerSubStringsIndexes = new Stack<>();

        findSubStringsAndTheirLongerIndexes(string, subStrings, longerSubStringsIndexes);

        return findOptimalTag(string, MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG, subStrings, longerSubStringsIndexes);
    }

    private void findSubStringsAndTheirLongerIndexes(String string, List<String> subStrings, Stack<Integer> longestSubIndexes) {
        int capitalLetterIndex = 0;
        longestSubIndexes.push(0);

        for (char letter : string.toCharArray()) {

            final boolean newCapitalLetterEncountered;
            final int indexOfLongestSub = longestSubIndexes.peek();

            if (isCapitalLetter(letter)) {
                newCapitalLetterEncountered = true;
                subStrings.add(String.valueOf(letter));
            } else {
                newCapitalLetterEncountered = false;
                String camelSub = subStrings.get(capitalLetterIndex);
                camelSub += String.valueOf(letter);
                subStrings.set(capitalLetterIndex, camelSub);
            }

            if ((subStrings.get(capitalLetterIndex).length() >= subStrings.get(indexOfLongestSub).length()) && (capitalLetterIndex > indexOfLongestSub)) {
                longestSubIndexes.push(capitalLetterIndex);
            }

            if (newCapitalLetterEncountered) {
                capitalLetterIndex++;
            }
        }
    }

    private String findOptimalTag(String tag, int MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG, List<String> tagSubStrings, Stack<Integer> longerTagSubStringsIndexes) {
        final int length = tag.length();

        optimal_tag_search:
        do {
            if (longerTagSubStringsIndexes.empty()) {
                tag.substring(0, MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
                break optimal_tag_search;
            } else {
                tag = getShorterTagWithLongestSubstringDecreasedByOne(tagSubStrings, longerTagSubStringsIndexes);
            }
        } while (length > MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        return tag;
    }

    private String getShorterTagWithLongestSubstringDecreasedByOne(List<String> subStrings, Stack<Integer> longerSubStringsIndexes) {
        final int longestSubIndex = longerSubStringsIndexes.pop();
        String longestSub = subStrings.get(longestSubIndex);
        subStrings.set(longestSubIndex, longestSub.substring(0, longestSub.length() - 1));
        return concatStrings(subStrings);
    }

    private String concatStrings(Iterable<String> strings) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String s : strings) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    private boolean isCapitalLetter(char c) {
        final int FIRST_ASCII_CAPITAL_LETTER_CHAR = 65;
        final int LAST_ASCII_CAPITAL_LETTER_CHAR = 90;
        return c >= FIRST_ASCII_CAPITAL_LETTER_CHAR && c <= LAST_ASCII_CAPITAL_LETTER_CHAR;
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}

