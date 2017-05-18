package com.matteoveroni.wordsremember.main_menu;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.online.WebDictionaryReaderService;

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

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebDictionaryReaderService.TRANSLTR_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WebDictionaryReaderService apiService = retrofit.create(WebDictionaryReaderService.class);

        Call<Void> call = apiService.getTranslation("ciao", "en");
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                Log.i(TagGenerator.tag(MainMenuPresenter.class), "response STATUS_CODE: " + statusCode);
//                User user = response.body();
                Log.i(TagGenerator.tag(MainMenuPresenter.class), "response BODY: " + response.body());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TagGenerator.tag(MainMenuPresenter.class), t.getMessage());
            }

        });


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
