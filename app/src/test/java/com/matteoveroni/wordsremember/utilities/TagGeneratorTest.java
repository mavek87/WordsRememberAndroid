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
        class ValidCamelCaseClass {
        }

        final String tag = tagGenerator.generateTag(ValidCamelCaseClass.class);

        final String ERROR = "tag is not equal to ValidCamelCaseClass.class.getSimpleName() like expected";
        assertEquals(ERROR, ValidCamelCaseClass.class.getSimpleName(), tag);
    }

    @Test
    public void testGenerateValidTagWithFirstLetterCapitalUsingFirstNotCapitalClass() {
        class firstNotCapitalClass {
        }

        final String tag = tagGenerator.generateTag(firstNotCapitalClass.class);

        final String ERROR = "tag is not equal to FirstNotCapitalClass like expected";
        assertEquals(ERROR, "FirstNotCapitalClass", tag);
    }

    @Test
    public void testGenerateValidTagWithFirstLetterCapitalUsingAllLowercaseLettersClass() {
        class lowercaselettersclass {
        }
        final String generatedTag = tagGenerator.generateTag(lowercaselettersclass.class);

        final String ERROR = "tag is not equal to Lowercaselettersclass like expected";
        assertEquals(ERROR, "Lowercaselettersclass", generatedTag);
    }

    @Test
    public void testGenerateTagForMuchLongCamelCaseClass() {
        class TooMuchLongCamelCaseClass {
        }

        final String generatedTag = tagGenerator.generateTag(TooMuchLongCamelCaseClass.class);

        final String ERROR = "tag is not equal to TooMuchLongCameCaseClas like expected";
        assertEquals(ERROR, "TooMuchLongCameCaseClas", generatedTag);
    }

    @Test
    public void testGenerateTagForAllUppercaseShortClass() {
        class ALLUPPERCASESHORTCLASS {
        }

        final String generatedTag = tagGenerator.generateTag(ALLUPPERCASESHORTCLASS.class);

        final String ERROR = "tag is not equal to ALLUPPERCASESHORTCLASS like expected";
        assertEquals(ERROR, "ALLUPPERCASESHORTCLASS", generatedTag);
    }

    @Test
    public void testGenerateTagForAllUppercaseTooMuchLongClass() {
        class ALLUPPERCASETOOMUCHLONGCLASS {
        }

        final String generatedTag = tagGenerator.generateTag(ALLUPPERCASETOOMUCHLONGCLASS.class);

        final String ERROR = "tag is not equal to ALLUPPERCASETOOMUCHLONG like expected";
        assertEquals(ERROR, "ALLUPPERCASETOOMUCHLONG", generatedTag);
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

        final String generatedTag = tagGenerator.generateTag(AaaaaaBbbbCccccDdddddEeeeee.class);

        final String ERROR = "tag is not equal to AaaaaBbbbCccccDddddEeee like expected";
        assertEquals(ERROR, "AaaaaBbbbCccccDddddEeee", generatedTag);
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

        final String generatedTag = tagGenerator.generateTag(AaaaaaaaaaaBbbbCcccccccDdddddEeeeee.class);

        final String ERROR = "tag is not equal to AaaaaBbbbCccccDddddEeee like expected";
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

        final String generatedTag = tagGenerator.generateTag(AAaaaaBbbbCcCCcccDdddddEEEeee.class);

        final String ERROR = "tag is not equal to AAaaaBbbbCcCCccDddEEEee like expected";
        assertEquals(ERROR, "AAaaaBbbbCcCCccDddEEEee", generatedTag);
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

        final String generatedTag = tagGenerator.generateTag(AAaaaaBbbbCcCcCcccDdddddEEEeee.class);

        final String ERROR = "tag is not equal to AAaaaBbbCcCcCccDddEEEee like expected";
        assertEquals(ERROR, "AAaaaBbbCcCcCccDddEEEee", generatedTag);
    }
}
