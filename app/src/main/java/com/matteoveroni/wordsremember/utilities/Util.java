package com.matteoveroni.wordsremember.utilities;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    /**
     * Method for creating Android Tags for a given class. Android TAG's text length can't exceed
     * 23 characters.
     *
     * @param clazz
     * @return the corresponding TAG for the passed class
     */
    public static String generateTag(Class clazz) throws Exception {
        final int MAX_LETTERS_FOR_TAG = 23;

        final String className = clazz.getSimpleName();
        final int classNameLength = className.length();

        if (classNameLength < 1) {
            throw new Exception("Class name length is 0");
        }

        final char classNameFirstLetter = className.charAt(0);
        if (!isCapitalLetter(classNameFirstLetter)) {
            throw new Exception("The name of the class doesn\'t use camel case notation. Impossible to generate a valid TAG");
        }

        if (classNameLength <= MAX_LETTERS_FOR_TAG) {
            return className;
        } else {

            final List<String> camelSub = new ArrayList<>();
            int subIndex = 0;
            int indexOfSubWithMaxLength = 0;
            String reducedTag = "";

            for (char c : className.toCharArray()) {
                boolean newCapitalLetterEncountered;
                if (isCapitalLetter(c)) {
                    reducedTag += "" + c;
                    camelSub.add("" + c);
                    newCapitalLetterEncountered = true;
                } else {
                    String substring = camelSub.get(subIndex);
                    substring += "" + c;
                    camelSub.set(subIndex, substring);
                    newCapitalLetterEncountered = false;
                }

                if ((camelSub.get(subIndex).length() >= camelSub.get(indexOfSubWithMaxLength).length()) && (subIndex > indexOfSubWithMaxLength)) {
                    indexOfSubWithMaxLength = subIndex;
                }

                if (newCapitalLetterEncountered) {
                    subIndex++;
                }
            }

//            if (reducedTag.length() >= MAX_LETTERS_FOR_TAG) {
//                return reducedTag.substring(0, MAX_LETTERS_FOR_TAG);
//            } else {
//
//            }

            while(reducedTag.length() > MAX_LETTERS_FOR_TAG){
                String longestCamelSub = camelSub.get(indexOfSubWithMaxLength);
            }
        }
        return reducedTag;
    }

    private static final boolean isCapitalLetter(char c) {
        final int FIRST_ASCII_CAPITAL_LETTER_CHAR = 65;
        final int LAST_ASCII_CAPITAL_LETTER_CHAR = 90;
        return c >= FIRST_ASCII_CAPITAL_LETTER_CHAR || c <= LAST_ASCII_CAPITAL_LETTER_CHAR;
    }
}
