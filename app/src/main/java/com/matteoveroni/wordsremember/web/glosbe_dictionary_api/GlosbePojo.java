package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public class GlosbePojo {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("tuc")
    @Expose
    private List<Tuc> tuc = null;

    @SerializedName("phrase")
    @Expose
    private String phrase;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("dest")
    @Expose
    private String dest;

//    @SerializedName("authors")
//    @Expose
//    private Authors authors;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Tuc> getTuc() {
        return tuc;
    }

    public void setTuc(List<Tuc> tuc) {
        this.tuc = tuc;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    //    public Authors getAuthors() {
    //        return authors;
    //    }
    //
    //    public void setAuthors(Authors authors) {
    //        this.authors = authors;
    //    }
}