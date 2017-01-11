package com.matteoveroni.wordsremember.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                tag = new CamelCaseStringShrinker().shrink(tag, MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
            }
        }
        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //HELPER METHODS
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // CamelCaseStringShrinker PRIVATE CLASS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class CamelCaseStringShrinker {
        private int totalSubstringsLetters;

        /**
         * @param string    a passed camel case string to shrink
         * @param maxLength Max length of the given string. It MUST be less than string.length()
         * @return optimal shrunk substring of the original string passed
         */
        public String shrink(String string, int maxLength) {
            totalSubstringsLetters = 0;
            Map<Integer, List<String>> mapOfCamelCaseSubstringsWithTheirLength = findSubstringsWithTheirLength(string);
            return calculateOptimalSubstring(mapOfCamelCaseSubstringsWithTheirLength, maxLength);
        }

        private Map<Integer, List<String>> findSubstringsWithTheirLength(String string) {
            final Map<Integer, List<String>> mapOfSubStringsWithTheirLength = new HashMap<>();
            String substring = "";
            for (char letter : string.toCharArray()) {
                if (isCapitalLetter(letter)) {
                    String newSubstring = String.valueOf(letter);
                    if (!substring.isEmpty()) {
                        saveSubString(substring, mapOfSubStringsWithTheirLength);
                    }
                    substring = newSubstring;
                } else {
                    substring += String.valueOf(letter);
                }
            }
            saveSubString(substring, mapOfSubStringsWithTheirLength);
            return mapOfSubStringsWithTheirLength;
        }

        private void saveSubString(String substring, Map<Integer, List<String>> mapOfSubstringsWithTheirLength) {
            int length = substring.length();
            if (length > 0) {
                if (mapOfSubstringsWithTheirLength.containsKey(length)) {
                    mapOfSubstringsWithTheirLength.get(length).add(substring);
                } else {
                    mapOfSubstringsWithTheirLength.put(length, new ArrayList<>(Arrays.asList(substring)));
                }
                totalSubstringsLetters += length;
            }
        }

        private String calculateOptimalSubstring(String string, Map<Integer, List<String>> mapOfSubstringsWithTheirLength, int maxLength) {
            int longestSubstringLength = string.length();
            optimal_substring_search:
            while (totalSubstringsLetters > maxLength || mapOfSubstringsWithTheirLength.keySet().size() <= 1) {
                string = decreaseLongestSubstringByOne(mapOfSubstringsWithTheirLength);
                longestSubstringLength = mapOfSubstringsWithTheirLength.keySet().size();
                totalSubstringsLetters--;
            }
            return string;
        }
    }

    private String decreaseLongestSubstringByOne(Map<Integer, List<String>> mapOfSubStringsWithTheirLength) {

    }


//    private String getShorterTagWithLongestSubstringDecreasedByOne(List<String> subStrings, Stack<Integer> longerSubStringsIndexes) {
//        final int longestSubIndex = longerSubStringsIndexes.pop();
//        String longestSub = subStrings.get(longestSubIndex);
//        subStrings.set(longestSubIndex, longestSub.substring(0, longestSub.length() - 1));
//        return concatStrings(subStrings);
//    }
//
//    private String concatStrings(Iterable<String> strings) {
//        final StringBuilder stringBuilder = new StringBuilder();
//        for (String s : strings) {
//            stringBuilder.append(s);
//        }
//        return stringBuilder.toString();
//    }
}

