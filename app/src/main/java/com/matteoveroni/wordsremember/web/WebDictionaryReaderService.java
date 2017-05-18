package com.matteoveroni.wordsremember.web;

import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.GlosbePojo;
import com.matteoveroni.wordsremember.web.transltr_dictionary_api.WebTranslation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Matteo Veroni
 */

public interface WebDictionaryReaderService {

    //    http://transltr.org/api/translate?text=ciao&to=en;
    public static final String TRANSLTR_WEB_DICTIONARY_BASE_URL = "http://transltr.org/api/";

    //    https://glosbe.com/gapi/translate?from=ita&dest=eng&format=json&phrase=translitterazione&pretty=true
    public static final String GLOSBE_WEB_DICTIONARY_BASE_URL = "https://glosbe.com/gapi/";

    @GET("translate")
    Call<WebTranslation> getTranslation(@Query("text") String wordToTranslate, @Query("to") String translationLanguage);

    @GET("translate")
    Call<GlosbePojo> getGlosbeTranslation(
            @Query("from") String locale_from,
            @Query("dest") String locale_dest,
            @Query("format") String content_type_format,
            @Query("phrase") String stringToTranslate,
            @Query("pretty") String boolean_isJsonPretty
    );
}
