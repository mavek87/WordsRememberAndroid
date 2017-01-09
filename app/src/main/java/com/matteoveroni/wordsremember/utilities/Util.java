package com.matteoveroni.wordsremember.utilities;

public class Util {

    /**
     * Method for creating Android Tags for a given class. Android TAG's text length can't exceed
     * 23 characters.
     *
     * @param clazz
     * @return the corresponding TAG for the passed class
     */
    public static String generateTag(Class clazz) {
        final String className = clazz.getSimpleName();
        final int classNameLength = className.length();
        return (classNameLength <= 23) ? className : className.substring(0, 22);
    }
}
