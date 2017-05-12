package com.matteoveroni.wordsremember.localization;

/**
 * @author Matteo Veroni
 */

public class FormattedLocaleString {

    private String string;
    private Object[] args;

    public FormattedLocaleString(String string) {
        this.string = string;
    }

    public FormattedLocaleString(String string, Object... args) {
        this.string = string;
        this.args = args;
    }

    public String getString() {
        return string;
    }

    public Object[] getArgs() {
        return args;
    }
}
