package com.matteoveroni.wordsremember.scene_settings.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BasePresentedActivityView;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.gamemodel.GameDifficulty;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_settings.presenter.SettingsPresenter;
import com.matteoveroni.wordsremember.scene_settings.presenter.SettingsPresenterFactory;
import com.matteoveroni.wordsremember.users.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class SettingsActivity extends BasePresentedActivityView implements SettingsView {

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

        printUsername();
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
        presenter.onGameDifficultySelected(GameDifficulty.EASY);
        radio_btn_easyGameDifficulty.toggle();
    }

    @Override
    @OnClick(R.id.radio_btn_secondGameDifficulty)
    public void mediumDifficultySelected() {
        presenter.onGameDifficultySelected(GameDifficulty.MEDIUM);
    }

    @Override
    @OnClick(R.id.radio_btn_thirdGameDifficulty)
    public void hardDifficultySelected() {
        presenter.onGameDifficultySelected(GameDifficulty.HARD);
    }

    @Override
    @OnClick(R.id.check_OnlineTranslationsCheck)
    public void onlineTranslationsCheckSelected() {
        boolean isChecked = check_OnlineTranslationsCheck.isChecked();
        presenter.onOnlineTranslationsCheckSelected(isChecked);
        Toast.makeText(this, isChecked + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkOnlineTranslationsCheckPreference(boolean check) {
        check_OnlineTranslationsCheck.setChecked(check);
    }

    @Override
    public void setLastGameDate(FormattedString lastGameDate) {
        lbl_last_game_date.setText(localize(lastGameDate));
    }

    private void printUsername() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        String str_usernameToPrint = getString(R.string.username) + ": ";

        String json_username = prefs.getString(Settings.USER_KEY, "");
        if (json_username.trim().isEmpty()) {
            str_usernameToPrint += "-";
        } else {
            User user = Json.getInstance().fromJson(json_username, User.class);
            str_usernameToPrint += user.getUsername();
        }

        lbl_username.setText(str_usernameToPrint);
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
}