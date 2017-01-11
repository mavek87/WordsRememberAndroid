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
    public void testNoLengthClassGenerateRandomUniqueTag() {
        final AnonymousClass anonymousClass = new AnonymousClass() {
        };
        final Class aClass = anonymousClass.getClass();

        final String generatedTag = tagGenerator.generateTag(aClass);

        final String ERROR1 = "generated tag is null";
        assertNotNull(ERROR1, generatedTag);

        final String ERROR2 = "generated tag doesn\'t start with the Tag suffix";
        assertEquals(ERROR2, TagGenerator.TAG, generatedTag.substring(0, 3));

        final String ERROR3 = "generated tag is not equal to Tag";
        assertNotSame(ERROR3, "Tag", generatedTag);

        final String ERROR4 = "generated tag is too long";
        assertTrue(ERROR4, generatedTag.length() <= TagGenerator.MAX_NUMBER_OF_LETTERS_FOR_ANDROID_TAG);
    }


    private class AnonymousClass {
    }

    private class ValidCamelCaseClass {
    }

    private class InvalidCamelCaseClassTooLong {
    }

}
