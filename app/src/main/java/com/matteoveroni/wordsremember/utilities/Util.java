package com.matteoveroni.wordsremember.utilities;

import java.lang.reflect.Field;

/**
 * @author Matteo Veroni
 */

public class Util {
    public static void resetSingleton(Class clazz, String fieldName) {
        Field instance;
        try {
            instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
