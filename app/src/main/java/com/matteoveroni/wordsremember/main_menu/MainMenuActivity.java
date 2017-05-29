package com.matteoveroni.wordsremember.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.view.activities.ManageVocablesActivity;
import com.matteoveroni.wordsremember.interfaces.base.BaseActivityMVP;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.quizgame.view.QuizGameActivity;
import com.matteoveroni.wordsremember.settings.view.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main Menu Activity
 *
 * @author Matteo Veroni
 */

public class MainMenuActivity extends BaseActivityMVP implements MainMenuView {

    public static final String TAG = TagGenerator.tag(MainMenuActivity.class);

    private MainMenuPresenter presenter;

    @BindView(R.id.main_menu_btn_start)
    Button btn_start;

    @BindView(R.id.main_menu_btn_manage_dictionary)
    Button btn_manage_dictionary;

    @BindView(R.id.main_menu_btn_settings)
    Button btn_settings;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new MainMenuPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (MainMenuPresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.main_menu));
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
}

