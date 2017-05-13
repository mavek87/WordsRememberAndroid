package com.matteoveroni.wordsremember;

/**
 * @author Matteo Veroni
 */

public class FormattedString {

    private String formattedString;
    private Object[] args;

    public FormattedString(String formattedString) {
        this.formattedString = formattedString;
    }

    public FormattedString(String formattedString, Object... args) {
        this.formattedString = formattedString;
        this.args = args;
    }

    public String getFormattedString() {
        return formattedString;
    }

    public Object[] getArgs() {
        return args;
    }
}
