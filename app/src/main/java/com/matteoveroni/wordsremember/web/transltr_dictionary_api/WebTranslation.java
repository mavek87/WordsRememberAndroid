package com.matteoveroni.wordsremember.web.transltr_dictionary_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@NoArgsConstructor
public class WebTranslation {

    @SerializedName("from")
    @Expose
    @Getter @Setter
    private String from;

    @SerializedName("to")
    @Expose
    @Getter @Setter
    private String to;

    @SerializedName("text")
    @Expose
    @Getter @Setter
    private String text;

    @SerializedName("translationText")
    @Expose
    @Getter @Setter
    private String translationText;

}