package com.matteoveroni.wordsremember.web.glosbe_dictionary_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@NoArgsConstructor
public class Meaning {

    @SerializedName("language")
    @Expose
    @Getter
    @Setter
    private String language;

    @SerializedName("text")
    @Expose
    @Getter
    @Setter
    private String text;
}