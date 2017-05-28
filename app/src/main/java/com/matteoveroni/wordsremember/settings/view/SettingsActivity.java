package com.matteoveroni.wordsremember.settings.view;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenter;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class SettingsActivity extends ActivityView implements SettingsView, LoaderManager.LoaderCallbacks<SettingsPresenter> {

    @BindView(R.id.lbl_gameVersion)
    TextView lbl_gameVersion;

    @BindView(R.id.radio_group_gameDifficulty)
    RadioGroup radio_group_gameDifficulty;

    @BindView(R.id.radio_btn_firstGameDifficulty)
    RadioButton radio_btn_easyGameDifficulty;

    @BindView(R.id.radio_btn_secondGameDifficulty)
    RadioButton radio_btn_mediumGameDifficulty;

    @BindView(R.id.radio_btn_thirdGameDifficulty)
    RadioButton radio_btn_hardGameDifficulty;

    private SettingsPresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    @Override
    public Loader<SettingsPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new SettingsPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<SettingsPresenter> loader, SettingsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<SettingsPresenter> loader) {
        this.presenter = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.settings));
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
        lbl_gameVersion.setText(Str.concat(getString(R.string.game_version), ": ", WordsRemember.VERSION));
    }

    @Override
    @OnClick(R.id.radio_btn_firstGameDifficulty)
    public void firstGameDifficultySelected() {
        presenter.onGameDifficultySelected(QuizGameDifficulty.EASY);
    }

    @Override
    @OnClick(R.id.radio_btn_secondGameDifficulty)
    public void secondGameDifficultySelected() {
        presenter.onGameDifficultySelected(QuizGameDifficulty.MEDIUM);
    }

    @Override
    @OnClick(R.id.radio_btn_thirdGameDifficulty)
    public void thirdGameDifficultySelected() {
        presenter.onGameDifficultySelected(QuizGameDifficulty.HARD);
    }
}