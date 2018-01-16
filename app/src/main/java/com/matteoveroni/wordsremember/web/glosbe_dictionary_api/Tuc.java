package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

/**
 * @author Matteo Veroni
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Tuc {

    @SerializedName("phrase")
    @Expose
    @Getter @Setter
    private Phrase phrase;

    @SerializedName("meaningId")
    @Expose
    @Getter @Setter
    private long meaningId;

    @SerializedName("meanings")
    @Expose
    @Getter @Setter
    private List<Phrase> meanings;

    @SerializedName("authors")
    @Expose
    @Getter @Setter
    private List<Integer> authors;
}