package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@NoArgsConstructor
public class GlosbePojo {

    @SerializedName("result")
    @Expose
    @Getter @Setter
    private String result;

    @SerializedName("tuc")
    @Expose
    @Getter @Setter
    private List<Tuc> tuc = null;

    @SerializedName("phrase")
    @Expose
    @Getter @Setter
    private String phrase;

    @SerializedName("from")
    @Expose
    @Getter @Setter
    private String from;

    @SerializedName("dest")
    @Expose
    @Getter @Setter
    private String dest;

//    @SerializedName("authors")
//    @Expose
//    private Authors authors;
}