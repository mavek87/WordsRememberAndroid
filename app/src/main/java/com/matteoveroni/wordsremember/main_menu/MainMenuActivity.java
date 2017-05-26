package com.matteoveroni.wordsremember.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.matteoveroni.wordsremember.dictionary.view.activities.ManageVocablesActivity;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameActivity;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.settings.view.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main Menu Activity
 */
public class MainMenuActivity extends AppCompatActivity implements MainMenuView, LoaderManager.LoaderCallbacks<MainMenuPresenter> {

    private MainMenuPresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    @BindView(R.id.main_menu_btn_start)
    Button btn_start;

    @BindView(R.id.main_menu_btn_manage_dictionary)
    Button btn_manage_dictionary;

    @BindView(R.id.main_menu_btn_settings)
    Button btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        presenter.destroy();
        super.onStop();
    }

    @OnClick(R.id.main_menu_btn_start)
    public void onButtonStartClicked() {
        presenter.onButtonStartClicked();
    }

    @Override
    public void startNewQuizGame() {
        startActivity(new Intent(getApplicationContext(), QuizGameActivity.class));
    }

    @OnClick(R.id.main_menu_btn_manage_dictionary)
    public void onButtonManageDictionaryClicked() {
        presenter.onButtonManageDictionaryClicked();
    }

    @Override
    public void startDictionaryManagement() {
        startActivity(new Intent(getApplicationContext(), ManageVocablesActivity.class));
    }


    @OnClick(R.id.main_menu_btn_settings)
    public void onButtonSettingsClicked() {
        presenter.onButtonSettingsClicked();
    }

    @Override
    public void startSettings() {
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    @Override
    public Loader<MainMenuPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new MainMenuPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<MainMenuPresenter> loader, MainMenuPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<MainMenuPresenter> loader) {
        presenter = null;
    }
}

