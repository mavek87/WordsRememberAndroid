package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

/**
 * @author Matteo Veroni
 */

public enum ContentType {
    JSON("json"),
    XML("xml");

    private final String formatName;

    ContentType(String s) {
        formatName = s;
    }

    public String getName() {
        return formatName;
    }
}
