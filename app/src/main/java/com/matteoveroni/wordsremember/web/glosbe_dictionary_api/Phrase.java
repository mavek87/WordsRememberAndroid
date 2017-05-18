package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Matteo Veroni
 */

public class Phrase {

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("text")
    @Expose
    private String text;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}