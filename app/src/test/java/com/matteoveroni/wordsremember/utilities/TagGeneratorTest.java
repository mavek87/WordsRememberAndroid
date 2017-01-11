package com.matteoveroni.wordsremember.utilities;

import org.junit.Test;

/**
 * Created by Matteo Veroni
 */

public class TagGeneratorTest {

    private TagGenerator tagGenerator;

    @Test
    public void test1() {
        tagGenerator = new TagGenerator(ValidCamelCaseClass.class);
    }

    private class ValidCamelCaseClass {
    }

    private class InvalidCamelCaseClassTooLong {
    }

}
