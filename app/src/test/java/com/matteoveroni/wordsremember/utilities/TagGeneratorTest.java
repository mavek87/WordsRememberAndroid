package com.matteoveroni.wordsremember.utilities;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Matteo Veroni
 */

public class TagGeneratorTest {

    @Test
    public void testGenerateRandomUniqueTagUsingNoLengthAnonymousClass() {
        class AnonymousClass {
        }
        final AnonymousClass anonymousClass = new AnonymousClass() {
        };
        final Class aClass = anonymousClass.getClass();

        final String tag = TagGenerator.tag(aClass);

        final String ERROR1 = "tag is null";
        assertNotNull(ERROR1, tag);

        final String ERROR2 = "tag doesn\'t start with the Tag suffix";
        assertEquals(ERROR2, TagGenerator.TAG_PREFIX, tag.substring(0, 3));

        final String ERROR3 = "tag is not equal to Tag";
        assertNotSame(ERROR3, "Tag", tag);

        final String ERROR4 = "tag is too long";
        assertTrue(ERROR4, tag.length() <= TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
    }

    @Test
    public void testGenerateValidTagUsingValidCamelCaseClass() {
        class ValidCamelCaseClass {
        }

        final String EXPECTED_RESULT = ValidCamelCaseClass.class.getSimpleName();
        assertTrue(EXPECTED_RESULT.length() <= TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String tag = TagGenerator.tag(ValidCamelCaseClass.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, tag);
    }

    @Test
    public void testGenerateValidTagWithFirstLetterCapitalUsingFirstNotCapitalClass() {
        class firstNotCapitalClass {
        }

        final String EXPECTED_RESULT = "FirstNotCapitalClass";
        assertTrue(EXPECTED_RESULT.length() <= TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String tag = TagGenerator.tag(firstNotCapitalClass.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, tag);
    }

    @Test
    public void testGenerateValidTagWithFirstLetterCapitalUsingAllLowercaseLettersClass() {
        class lowercaselettersclass {
        }

        final String EXPECTED_RESULT = "Lowercaselettersclass";
        assertTrue(EXPECTED_RESULT.length() <= TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(lowercaselettersclass.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    @Test
    public void testGenerateTagForMuchLongCamelCaseClass() {
        class TooMuchLongCamelCaseClass {
        }

        final String EXPECTED_RESULT = "TooMuchLongCameCaseClas";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(TooMuchLongCamelCaseClass.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    @Test
    public void testGenerateTagForAllUppercaseShortClass() {
        class ALLUPPERCASESHORTCLASS {
        }

        final String EXPECTED_RESULT = "ALLUPPERCASESHORTCLASS";
        assertTrue(EXPECTED_RESULT.length() < TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(ALLUPPERCASESHORTCLASS.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    @Test
    public void testGenerateTagForAllUppercaseTooMuchLongClass() {
        class ALLUPPERCASETOOMUCHLONGCLASS {
        }

        final String EXPECTED_RESULT = "ALLUPPERCASETOOMUCHLONG";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(ALLUPPERCASETOOMUCHLONGCLASS.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    /**
     * AaaaaaBbbbCccccDdddddEeeeee = 27 letters (4 letter longer than max)
     * Aaaaaa = 6
     * Bbbb = 4
     * Ccccc = 5
     * Dddddd = 6
     * Eeeeee = 6
     */
    @Test
    public void testGenerateTagForAaaaaaBbbbCccccDdddddEeeeeeClass() {
        class AaaaaaBbbbCccccDdddddEeeeee {
        }

        final String EXPECTED_RESULT = "AaaaaBbbbCccccDddddEeee";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(AaaaaaBbbbCccccDdddddEeeeee.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    /**
     * AaaaaaaaaaaBbbbCcccccccDdddddEeeeee = 35 letters (12 letter longer than max)
     * Aaaaaa = 11
     * Bbbb = 4
     * Ccccc = 8
     * Dddddd = 6
     * Eeeeee = 6
     */
    @Test
    public void testGenerateTagForAaaaaaaaaaaBbbbCcccccccDdddddEeeeeeClass() {
        class AaaaaaaaaaaBbbbCcccccccDdddddEeeeee {
        }

        final String EXPECTED_RESULT = "AaaaaBbbbCccccDddddEeee";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(AaaaaaaaaaaBbbbCcccccccDdddddEeeeee.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, "AaaaaBbbbCccccDddddEeee", generatedTag);
    }

    /**
     * AAaaaaBbbbCcCCcccDdddddEEEeee = 29 letters (6 letter longer than max)
     * A = 1
     * Aaaaa = 5     -1 = 4  Aaaa
     * Bbbb = 4
     * Cc = 2
     * C = 1
     * Cccc = 4      -1 = 3  Ccc
     * Dddddd = 6    -3 = 3  Ddd
     * E = 1
     * E = 1
     * Eeee = 4      -1 = 3 Eee
     */
    @Test
    public void testGenerateTagFoAAaaaaBbbbCcCCcccDdddddEEEeeeClass() {
        class AAaaaaBbbbCcCCcccDdddddEEEeee {
        }

        final String EXPECTED_RESULT = "AAaaaBbbbCcCCccDddEEEee";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(AAaaaaBbbbCcCCcccDdddddEEEeee.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    /**
     * AAaaaaBbbbCcCcCcccDdddddEEEeee = 31 letters (8 letter longer than max)
     * A = 1
     * Aaaaa = 5     -1 = 4  Aaaa
     * Bbbb = 4      -1 = 3  Bbb
     * Cc = 2
     * Cc = 2
     * Cccc = 4      -1 = 3  Ccc
     * Ddddddd = 7   -4 = 3  Ddd
     * E = 1
     * E = 1
     * Eeee = 4      -1 = 3 Eee
     */
    @Test
    public void testGenerateTagFoAAaaaaBbbbCcCcCcccDdddddEEEeeeClass() {
        class AAaaaaBbbbCcCcCcccDdddddEEEeee {
        }

        final String EXPECTED_RESULT = "AAaaaBbbCcCcCccDddEEEee";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(AAaaaaBbbbCcCcCcccDdddddEEEeee.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }

    /**
     * aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa  = 60 letters (17 letter longer than max)
     */
    @Test
    public void testGenerateTagForaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa() {
        class aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa {
        }

        final String EXPECTED_RESULT = "Aaaaaaaaaaaaaaaaaaaaaaa";
        assertTrue(EXPECTED_RESULT.length() == TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);

        final String generatedTag = TagGenerator.tag(aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.class);

        final String ERROR = "tag is not equal to " + EXPECTED_RESULT + " like expected";
        assertEquals(ERROR, EXPECTED_RESULT, generatedTag);
    }
}
