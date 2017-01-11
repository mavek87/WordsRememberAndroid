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

    private TagGenerator tagGenerator = new TagGenerator();

    @Test
    public void testGenerateRandomUniqueTagUsingNoLengthAnonymousClass() {
        class AnonymousClass {
        }
        final AnonymousClass anonymousClass = new AnonymousClass() {
        };
        final Class aClass = anonymousClass.getClass();

        final String tag = tagGenerator.generateTag(aClass);

        final String ERROR1 = "tag is null";
        assertNotNull(ERROR1, tag);

        final String ERROR2 = "tag doesn\'t start with the Tag suffix";
        assertEquals(ERROR2, TagGenerator.TAG, tag.substring(0, 3));

        final String ERROR3 = "tag is not equal to Tag";
        assertNotSame(ERROR3, "Tag", tag);

        final String ERROR4 = "tag is too long";
        assertTrue(ERROR4, tag.length() <= TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
    }

    @Test
    public void testGenerateValidTagUsingValidCamelCaseClass() {
        final String tag = tagGenerator.generateTag(ValidCamelCaseClass.class);

        final String ERROR1 = "tag is null";
        assertNotNull(ERROR1, tag);

        final String ERROR2 = "tag is not equal to ValidCamelCaseClass.class.getSimpleName() like expected";
        assertEquals(ERROR2, ValidCamelCaseClass.class.getSimpleName(), tag);
    }

    @Test
    public void testGenerateValidTagWithFirstLetterCapitalUsingFirstNotCapitalClass() {
        class firstNotCapitalClass {
        }

        final String tag = tagGenerator.generateTag(firstNotCapitalClass.class);

        final String ERROR1 = "tag is null";
        assertNotNull(ERROR1, tag);

        final String ERROR2 = "tag is not equal to FirstNotCapitalClass like expected";
        assertEquals(ERROR2, "FirstNotCapitalClass", tag);
    }

    @Test
    public void testGenerateValidTagWithFirstLetterCapitalUsingAllLowercaseLettersClass() {
        class lowercaselettersclass {
        }
        final String generatedTag = tagGenerator.generateTag(lowercaselettersclass.class);

        final String ERROR1 = "tag is null";
        assertNotNull(ERROR1, generatedTag);

        final String ERROR2 = "tag is not equal to Lowercaselettersclass like expected";
        assertEquals(ERROR2, "Lowercaselettersclass", generatedTag);
    }

    private class ValidCamelCaseClass {
    }

    private class InvalidCamelCaseClassTooLong {
    }

}
