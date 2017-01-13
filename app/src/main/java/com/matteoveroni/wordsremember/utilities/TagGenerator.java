package com.matteoveroni.wordsremember.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matteo Veroni
 */

public final class TagGenerator {

    public static final String TAG_PREFIX = "Tag";
    public static final int MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG = 23;

    /**
     * Method which generates Android Tags for a given class. Android TAG's length can't exceed
     * 23 characters.
     *
     * @return the corresponding TAG_PREFIX for class passed to the constructor
     */
    public final String getTag(Class classInstance) {
        String tag = classInstance.getSimpleName();
        int tagLength = tag.length();
        if (tagLength < 1) {
            tag = generateRandomUniqueTag();
        } else {
            if (!isCapitalLetter(tag.charAt(0))) {
                tag = capitalizeFirstLetter(tag);
            }
            if (tagLength > MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG) {
                tag = new CamelCaseStringShrinker().shrink(tag, MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
            }
        }
        return tag;
    }

    /**********************************************************************************************/

    // CamelCaseStringShrinker  PRIVATE CLASS

    /**********************************************************************************************/

    private final class CamelCaseStringShrinker {

        private int totalSubstringsLetters;
        private int maxSubstringLength;

        /**
         * @param string    a passed camel case string to shrink
         * @param maxLength Max possible length. It MUST be less than string.length()
         * @return optimal shrunk substring of the original string passed
         */
        public String shrink(String string, int maxLength) {
            totalSubstringsLetters = 0;
            maxSubstringLength = 0;
            final List<String> substrings = findSubstrings(string);
            return calculateOptimalShrinkString(substrings, maxLength);
        }

        public List<String> findSubstrings(String string) {
            final List<String> substrings = new ArrayList<>();
            StringBuilder substringBuilder = null;
            for (char letter : string.toCharArray()) {
                if (isCapitalLetter(letter)) {
                    if (substringBuilder != null) {
                        saveSubstring(substringBuilder.toString(), substrings);
                    }
                    substringBuilder = new StringBuilder(String.valueOf(letter));
                } else {
                    substringBuilder.append(String.valueOf(letter));
                }
            }
            saveSubstring(substringBuilder.toString(), substrings);
            return substrings;
        }

        public String calculateOptimalShrinkString(List<String> substrings, int maxLength) {
            String optimalString = Str.concat(substrings);
            while (totalSubstringsLetters > maxLength) {

                maxSubstringLength = 1;
                int indexOfMaxSubstringLength = findIndexOfMaxSubstringLength(substrings);

                if (maxSubstringLength == 1) {
                    optimalString = optimalString.substring(0, maxLength);
                    break;
                } else {
                    String maxSubStringDecreased = getStringDecreasedByOne(substrings.get(indexOfMaxSubstringLength));
                    substrings.set(indexOfMaxSubstringLength, maxSubStringDecreased);
                    totalSubstringsLetters--;
                    optimalString = Str.concat(substrings);
                }
            }
            return optimalString;
        }

        private int findIndexOfMaxSubstringLength(List<String> substrings) {
            int indexOfMaxSubstringLength = substrings.size();

            for (int i = substrings.size() - 1; i >= 0; i--) {
                String substring = substrings.get(i);
                if (substring.length() > maxSubstringLength) {
                    maxSubstringLength = substring.length();
                    indexOfMaxSubstringLength = i;
                }
            }
            return indexOfMaxSubstringLength;
        }

        private String getStringDecreasedByOne(String string) {
            if (string.length() > 1) {
                string = removeLastLetter(string);
            }
            return string;
        }

        private void saveSubstring(String substring, List<String> substrings) {
            int length = substring.length();
            if (length > 0) {
                if (length > maxSubstringLength) {
                    maxSubstringLength = length;
                }
                substrings.add(substring);
                totalSubstringsLetters += length;
            }
        }
    }

    /**********************************************************************************************/

    // HELPER METHODS

    /**********************************************************************************************/

    /**
     * Useful material:
     * <p>
     * http://stackoverflow.com/questions/20994768/how-to-reduce-length-of-uuid-generated-using-randomuuid
     * </p>
     */
    private String generateRandomUniqueTag() {
        final UUID uuid = UUID.randomUUID();
        String str_uuid = Long.toString(uuid.getLeastSignificantBits(), 94);
        return TAG_PREFIX + str_uuid;
    }

    private boolean isCapitalLetter(char c) {
        final int FIRST_ASCII_CAPITAL_LETTER_CHAR = 65;
        final int LAST_ASCII_CAPITAL_LETTER_CHAR = 90;
        return c >= FIRST_ASCII_CAPITAL_LETTER_CHAR && c <= LAST_ASCII_CAPITAL_LETTER_CHAR;
    }

    private String capitalizeFirstLetter(String string) {
        return isStringValid(string)
                ? string.substring(0, 1).toUpperCase() + string.substring(1)
                : string;
    }

    private String removeLastLetter(String string) {
        return isStringValid(string)
                ? string.substring(0, string.length() - 1)
                : string;
    }

    private boolean isStringValid(String string) {
        return (string != null && string.length() > 0);
    }
}

