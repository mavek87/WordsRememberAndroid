package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

/**
 * @author Matteo Veroni
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tuc {

    @SerializedName("phrase")
    @Expose
    private Phrase phrase;

    @SerializedName("meaningId")
    @Expose
    private long meaningId;

    @SerializedName("meanings")
    @Expose
    private List<Phrase> meanings = null;

    @SerializedName("authors")
    @Expose
    private List<Integer> authors = null;

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public long getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(long meaningId) {
        this.meaningId = meaningId;
    }

    public List<Phrase> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Phrase> meanings) {
        this.meanings = meanings;
    }

    public List<Integer> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Integer> authors) {
        this.authors = authors;
    }

}