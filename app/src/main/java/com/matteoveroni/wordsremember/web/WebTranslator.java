package com.matteoveroni.wordsremember.web;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.ContentType;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.GlosbePojo;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.Phrase;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.Tuc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private volatile static WebTranslator WEB_TRANSLATOR_UNIQUE_INSTANCE;

    private final WebDictionaryReaderService apiService;

    private WebTranslator() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebDictionaryReaderService.GLOSBE_WEB_DICTIONARY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(WebDictionaryReaderService.class);
    }

    public static final WebTranslator getInstance() {
        if (WEB_TRANSLATOR_UNIQUE_INSTANCE == null) {
            synchronized (WebTranslator.class) {
                if (WEB_TRANSLATOR_UNIQUE_INSTANCE == null) {
                    WEB_TRANSLATOR_UNIQUE_INSTANCE = new WebTranslator();
                }
            }
        }
        return WEB_TRANSLATOR_UNIQUE_INSTANCE;
    }

    public void translate(Word vocableToTranslate, Locale locale_from, Locale locale_dest, final WebTranslatorListener listener) {
        Log.d(TAG, "TRANSLATION REQUEST => translate \'" + vocableToTranslate.getName() + "\' from \'" + locale_from + "\' to \'" + locale_dest + "\'");

        Call<GlosbePojo> request = apiService.getGlosbeTranslation(
                locale_from.getLanguage(),
                locale_dest.getLanguage(),
                ContentType.JSON.getName(),
                vocableToTranslate.getName().toLowerCase(),
                "true"
        );

        request.enqueue(new Callback<GlosbePojo>() {

            @Override
            public void onResponse(Call<GlosbePojo> request, Response<GlosbePojo> response) {
                final int statusCode = response.code();
                Log.d(TAG, "RESPONSE STATUS_CODE: " + statusCode);

                final GlosbePojo glosbePojo = response.body();
                Log.d(TAG, "GLOSBE_POJO JSONized: " + Json.getInstance().toJson(glosbePojo));

                final List<Word> translations = getTranslations(glosbePojo);
                listener.onTranslationCompletedSuccessfully(translations);
            }

            @Override
            public void onFailure(Call<GlosbePojo> request, Throwable t) {
                Log.w(TAG, t.getMessage());
                listener.onTranslationCompletedWithError(t);
            }

        });
    }

    private List<Word> getTranslations(GlosbePojo glosbePojo) {
        List<Tuc> tucs = glosbePojo.getTuc();
        List<Word> webTranslations = new ArrayList<>();

        for (Tuc tuc : tucs) {
            Phrase phrase = tuc.getPhrase();
            if (phrase != null && !phrase.getText().trim().isEmpty()) {
                webTranslations.add(new Word(phrase.getText()));
                Log.d(TAG, phrase.getText());
            }
        }
        return webTranslations;
    }
}
