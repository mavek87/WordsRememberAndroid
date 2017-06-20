package com.matteoveroni.wordsremember.settings.view;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenter;
import com.matteoveroni.wordsremember.settings.presenter.SettingsPresenterFactory;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class SettingsActivity extends BaseActivityPresentedView implements SettingsView {

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
        setLabelsForRadioButtons();
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
        String str_date = lastGameDate.toLocalDate().toString();
        String str_time = lastGameDate.getHourOfDay() + ":" + lastGameDate.getMinuteOfHour() + ":" + lastGameDate.getSecondOfMinute();
        lbl_last_game_date.setText(getString(R.string.lastGameDate) + ": " + str_date + " - " + str_time);
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

    private void setLabelsForRadioButtons() {
        String question = getString(R.string.question).toLowerCase();
        String questions = getString(R.string.questions).toLowerCase();

        int numberOfQuestionsForEasy = Settings.getNumberOfQuestionsForDifficulty(QuizGameDifficulty.EASY);
        String str_btn_EasyGameDifficulty = String.format(
                "%s (%s %s)",
                getString(R.string.easy),
                numberOfQuestionsForEasy,
                (numberOfQuestionsForEasy > 1) ? questions : question
        );
        radio_btn_easyGameDifficulty.setText(str_btn_EasyGameDifficulty);

        int numberOfQuestionsForMedium = Settings.getNumberOfQuestionsForDifficulty(QuizGameDifficulty.MEDIUM);
        String str_btn_MediumGameDifficulty = String.format(
                "%s (%s %s)",
                getString(R.string.medium),
                numberOfQuestionsForMedium,
                (numberOfQuestionsForMedium > 1) ? questions : question
        );
        radio_btn_mediumGameDifficulty.setText(str_btn_MediumGameDifficulty);

        int numberOfQuestionsForHard = Settings.getNumberOfQuestionsForDifficulty(QuizGameDifficulty.HARD);
        String str_btn_HardGameDifficulty = String.format(
                "%s (%s %s)",
                getString(R.string.hard),
                numberOfQuestionsForHard,
                (numberOfQuestionsForHard > 1) ? questions : question

        );
        radio_btn_hardGameDifficulty.setText(str_btn_HardGameDifficulty);
    }
}