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
    public static String generateTag(Class clazz) throws TagGenerator.ClassNameIsNotCamelCaseException, TagGenerator.ClassNameIsLongZeroCharacters {
        TagGenerator tagGenerator = new TagGenerator(clazz);
        return tagGenerator.generateTag();
    }
}
