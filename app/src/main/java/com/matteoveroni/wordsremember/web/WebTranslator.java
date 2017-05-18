package com.matteoveroni.wordsremember.web;

import android.support.annotation.NonNull;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.GlosbePojo;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.Phrase;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.Tuc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Matteo Veroni
 */

public class WebTranslator {

    private static final String TAG = TagGenerator.tag(WebTranslator.class);
    private final WebDictionaryReaderService apiService;

    public WebTranslator() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebDictionaryReaderService.GLOSBE_WEB_DICTIONARY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(WebDictionaryReaderService.class);
    }

    public void translate(String locale_from, String locale_dest, final Word vocableToTranslate, final WebTranslatorListener listener) {

        Call<GlosbePojo> request = apiService.getGlosbeTranslation(
                locale_from,
                locale_dest,
                "json",
                vocableToTranslate.getName(),
                "true"
        );

        request.enqueue(new Callback<GlosbePojo>() {

            @Override
            public void onResponse(Call<GlosbePojo> request, Response<GlosbePojo> response) {
                final int statusCode = response.code();
                Log.i(TAG, "response STATUS_CODE: " + statusCode);

                final GlosbePojo glosbePojo = response.body();
                Log.i(TAG, "GLOSBE POJO IN JSON: " + Json.getInstance().toJson(glosbePojo));

                final List<VocableTranslation> vocableTranslations = convertGlosbePojoInVocableTranslations(
                        glosbePojo, vocableToTranslate
                );

                listener.onTranslationCompletedSuccessfully(vocableTranslations);
            }

            @Override
            public void onFailure(Call<GlosbePojo> request, Throwable t) {
                Log.w(TAG, t.getMessage());
                listener.onTranslationCompletedWithError(t);
            }

        });
    }

    @NonNull
    private List<VocableTranslation> convertGlosbePojoInVocableTranslations(GlosbePojo glosbePojo, Word vocableToTranslate) {
        List<Tuc> tucs = glosbePojo.getTuc();
        List<VocableTranslation> webTranslations = new ArrayList<>();

        for (Tuc tuc : tucs) {
            Phrase phrase = tuc.getPhrase();
            if (phrase != null && !phrase.getText().trim().isEmpty()) {
                webTranslations.add(new VocableTranslation(vocableToTranslate, new Word(phrase.getText())));
                Log.i(TAG, phrase.getText());
            }
        }
        return webTranslations;
    }
}
