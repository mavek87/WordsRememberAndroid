package com.matteoveroni.wordsremember.main_menu;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.web.glosbe_dictionary_api.GlosbePojo;
import com.matteoveroni.wordsremember.web.WebDictionaryReaderService;
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
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 * <p>
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class MainMenuPresenter implements Presenter {

    private MainMenuView view;

    @Override
    public void attachView(Object view) {
        this.view = (MainMenuView) view;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebDictionaryReaderService.GLOSBE_WEB_DICTIONARY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebDictionaryReaderService apiService = retrofit.create(WebDictionaryReaderService.class);

//        Call<WebTranslation> call = apiService.getTranslation("ciao", "en");
//        call.enqueue(new Callback<WebTranslation>() {
//
//            @Override
//            public void onResponse(Call<WebTranslation> call, Response<WebTranslation> response) {
//                int statusCode = response.code();
//                Log.i(TagGenerator.tag(MainMenuPresenter.class), "response STATUS_CODE: " + statusCode);
//                WebTranslation translation = response.body();
//                Log.i(TagGenerator.tag(MainMenuPresenter.class), "TRANSLATION: " + Json.getInstance().toJson(translation));
//            }
//
//            @Override
//            public void onFailure(Call<WebTranslation> call, Throwable t) {
//                Log.e(TagGenerator.tag(MainMenuPresenter.class), t.getMessage());
//            }
//
//        });

        Call<GlosbePojo> call = apiService.getGlosbeTranslation("ita", "eng", "json", "traslitterazione", "true");
        call.enqueue(new Callback<GlosbePojo>() {

            @Override
            public void onResponse(Call<GlosbePojo> call, Response<GlosbePojo> response) {
                int statusCode = response.code();
                Log.i(TagGenerator.tag(MainMenuPresenter.class), "response STATUS_CODE: " + statusCode);
                GlosbePojo glosbePojo = response.body();
                Log.i(TagGenerator.tag(MainMenuPresenter.class), "FIRST TRANSLATION: " + Json.getInstance().toJson(glosbePojo));

                List<Tuc> tucs = glosbePojo.getTuc();
                List<String> webTranslations = new ArrayList<>();

                for (Tuc tuc : tucs) {
                    Phrase phrase = tuc.getPhrase();
                    if (phrase != null && !phrase.getText().trim().isEmpty()) {
                        webTranslations.add(phrase.getText());
                        Log.i(TagGenerator.tag(MainMenuPresenter.class), phrase.getText());
                    }
                }

            }

            @Override
            public void onFailure(Call<GlosbePojo> call, Throwable t) {
                Log.e(TagGenerator.tag(MainMenuPresenter.class), t.getMessage());
            }

        });

//        Call<Void> call = apiService.getTranslationsJson("eng", "ita", "json", "tomorrow", "true");
//        call.enqueue(new Callback<Void>() {
//
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                int statusCode = response.code();
//                Log.i(TagGenerator.tag(MainMenuPresenter.class), "response STATUS_CODE: " + statusCode);
////                List<Meaning> meanings = response.body();
//                Log.i(TagGenerator.tag(MainMenuPresenter.class), "FIRST TRANSLATION: " + response.body());
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.e(TagGenerator.tag(MainMenuPresenter.class), t.getMessage());
//            }
//
//        });


    }

    @Override
    public void destroy() {
        this.view = null;
    }

    void onButtonManageDictionaryClicked() {
        view.startDictionaryManagement();
    }

    public void onButtonStartClicked() {
        view.startNewQuizGame();
    }
}
