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
import com.matteoveroni.wordsremember.interfaces.base.BaseActivityMVP;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenter;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenterFactory;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class SettingsActivity extends BaseActivityMVP implements SettingsView {

    @BindView(R.id.lbl_gameVersion)
    TextView lbl_gameVersion;

    @BindView(R.id.lbl_deviceLocale)
    TextView lbl_deviceLocale;

    @BindView(R.id.lbl_last_game_date)
    TextView lbl_last_game_date;

    @BindView(R.id.radio_group_gameDifficulty)
    RadioGroup radio_group_gameDifficulty;

    @BindView(R.id.radio_btn_firstGameDifficulty)
    RadioButton radio_btn_easyGameDifficulty;

    @BindView(R.id.radio_btn_secondGameDifficulty)
    RadioButton radio_btn_mediumGameDifficulty;

    @BindView(R.id.radio_btn_thirdGameDifficulty)
    RadioButton radio_btn_hardGameDifficulty;

    private SettingsPresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new SettingsPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (SettingsPresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setupAndShowToolbar(getString(R.string.settings));

        printGameVersion();
        printDeviceLocale();
    }

    private void printGameVersion() {
        lbl_gameVersion.setText(Str.concat(getString(R.string.version), ": ", WordsRemember.VERSION));
    }

    private void printDeviceLocale() {
        String text_lbl_deviceLocale = lbl_deviceLocale.getText().toString();
        if (!text_lbl_deviceLocale.trim().isEmpty()) {
            String str_deviceLocale = Str.concat(
                    lbl_deviceLocale.getText().toString(),
                    ": ",
                    LocaleTranslator.getLocale(getApplicationContext()).toString()
            );
            lbl_deviceLocale.setText(str_deviceLocale);
        }
    }


    @Override
    public void toggleEasyDifficulty() {
        radio_btn_easyGameDifficulty.toggle();
    }

    @Override
    public void toggleMediumDifficulty() {
        radio_btn_mediumGameDifficulty.toggle();

    }

    @Override
    public void toggleHardDifficulty() {
        radio_btn_hardGameDifficulty.toggle();
    }

    @Override
    @OnClick(R.id.radio_btn_firstGameDifficulty)
    public void easyDifficultySelected() {
        presenter.onGameDifficultySelected(QuizGameDifficulty.EASY);
        radio_btn_easyGameDifficulty.toggle();
    }

    @Override
    @OnClick(R.id.radio_btn_secondGameDifficulty)
    public void mediumDifficultySelected() {
        presenter.onGameDifficultySelected(QuizGameDifficulty.MEDIUM);
    }

    @Override
    @OnClick(R.id.radio_btn_thirdGameDifficulty)
    public void hardDifficultySelected() {
        presenter.onGameDifficultySelected(QuizGameDifficulty.HARD);
    }

    @Override
    public void setLastGameDate(DateTime lastGameDate) {
        lbl_last_game_date.setText(getString(R.string.lastGameDate) + ": "
                + lastGameDate.toLocalDate().toString()
                + " - "
                + lastGameDate.getHourOfDay() + ":"
                + lastGameDate.getMinuteOfHour() + ":"
                + lastGameDate.getSecondOfMinute()
        );
    }
}