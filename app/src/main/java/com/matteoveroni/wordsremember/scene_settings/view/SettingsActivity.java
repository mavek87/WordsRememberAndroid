package com.matteoveroni.wordsremember.scene_settings.view;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_settings.presenter.SettingsPresenter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class SettingsActivity extends AbstractPresentedActivityView implements SettingsView {

    @BindView(R.id.lbl_dictionary)
    TextView lbl_dictionary;
    @BindView(R.id.lbl_username)
    TextView lbl_username;
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
    @BindView(R.id.check_OnlineTranslationsCheck)
    CheckBox check_OnlineTranslationsCheck;

    private SettingsPresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.SETTINGS_PRESENTER_FACTORY);
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
    }

    @Override
    public void showDictionaryName(String dictionaryName) {
        lbl_dictionary.setText(String.format("%s: %s", getString(R.string.dictionary), dictionaryName));
    }

    @Override
    public void showUsername(String username) {
        lbl_username.setText(String.format("%s: %s", getString(R.string.username), username));
    }

    @Override
    public void showDeviceLocale() {
        Locale locale = LocaleTranslator.getLocale(getApplicationContext());
        lbl_deviceLocale.setText(String.format("%s: %s", getString(R.string.device_locale), LocaleTranslator.stringifyLocale(locale)));
    }

    @Override
    public void showLastGameDate(String lastGameDate) {
        lbl_last_game_date.setText(String.format("%s: %s", getString(R.string.last_game_date), lastGameDate));
    }

    @Override
    public void showGameVersion(String gameVersion) {
        lbl_gameVersion.setText(String.format("%s: %s", getString(R.string.version), gameVersion));
    }

    @Override
    public void showGameDifficultyRadioButtonLabels() {
        String question = getString(R.string.question).toLowerCase();
        String questions = getString(R.string.questions).toLowerCase();

        int numberOfEasyQuestions = Settings.getNumberOfQuestionsForDifficulty(GameDifficulty.EASY);
        String str_btn_EasyGameDifficulty = String.format(
                "%s (%s %s)",
                getString(R.string.easy),
                numberOfEasyQuestions,
                (numberOfEasyQuestions > 1) ? questions : question
        );
        radio_btn_easyGameDifficulty.setText(str_btn_EasyGameDifficulty);

        int numberOfMediumQuestions = Settings.getNumberOfQuestionsForDifficulty(GameDifficulty.MEDIUM);
        String str_btn_MediumGameDifficulty = String.format(
                "%s (%s %s)",
                getString(R.string.medium),
                numberOfMediumQuestions,
                (numberOfMediumQuestions > 1) ? questions : question
        );
        radio_btn_mediumGameDifficulty.setText(str_btn_MediumGameDifficulty);

        int numberOfHardQuestions = Settings.getNumberOfQuestionsForDifficulty(GameDifficulty.HARD);
        String str_btn_HardGameDifficulty = String.format(
                "%s (%s %s)",
                getString(R.string.hard),
                numberOfHardQuestions,
                (numberOfHardQuestions > 1) ? questions : question

        );
        radio_btn_hardGameDifficulty.setText(str_btn_HardGameDifficulty);
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
    public void onEasyDifficultySelected() {
        presenter.onGameDifficultyChanged(GameDifficulty.EASY);
        radio_btn_easyGameDifficulty.toggle();
    }

    @Override
    @OnClick(R.id.radio_btn_secondGameDifficulty)
    public void onMediumDifficultySelected() {
        presenter.onGameDifficultyChanged(GameDifficulty.MEDIUM);
    }

    @Override
    @OnClick(R.id.radio_btn_thirdGameDifficulty)
    public void onHardDifficultySelected() {
        presenter.onGameDifficultyChanged(GameDifficulty.HARD);
    }

    @Override
    @OnClick(R.id.check_OnlineTranslationsCheck)
    public void onOnlineTranslationsCheckboxSelected() {
        boolean isChecked = check_OnlineTranslationsCheck.isChecked();
        presenter.onOnlineTranslationsCheckSelected(isChecked);
    }

    @Override
    public void checkOnlineTranslationsCheckPreference(boolean check) {
        check_OnlineTranslationsCheck.setChecked(check);
    }
}