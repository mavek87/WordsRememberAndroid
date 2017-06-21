package com.matteoveroni.wordsremember.settings.model;

import android.content.SharedPreferences;

import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.quizgame.business_logic.QuizGameDifficulty;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Matteo Veroni
 */

public class Settings {

    private final SharedPreferences prefs;

    public static final String GAME_DIFFICULTY_KEY = "game_difficulty_key";
    public static final String GAME_NUMBER_OF_QUESTIONS_KEY = "game_number_of_questions_key";
    public static final String LAST_GAME_DATE_KEY = "last_game_date_key";
    public static final String ONLINE_TRANSLATIONS_CHECK_KEY = "online_translations_check_key";
    public static final QuizGameDifficulty DEFAULT_DIFFICULTY = QuizGameDifficulty.EASY;
    public static final int DEFAULT_NUMBER_OF_QUESTIONS = getNumberOfQuestionsForDifficulty(DEFAULT_DIFFICULTY);

    public Settings(SharedPreferences prefs) {
        this.prefs = prefs;
        if (!prefs.contains(GAME_DIFFICULTY_KEY)) {
            setDifficulty(DEFAULT_DIFFICULTY);
        }
    }

    public Settings(SharedPreferences prefs, QuizGameDifficulty difficulty) {
        this.prefs = prefs;
        setDifficulty(difficulty);
    }

    public int getNumberOfQuestions() {
        return prefs.getInt(GAME_NUMBER_OF_QUESTIONS_KEY, DEFAULT_NUMBER_OF_QUESTIONS);
    }

    public void setNumberOfQuestions(int number) {
        if (number < 0)
            throw new IllegalArgumentException("Number of questions can\'t be negative");

        prefs.edit().putInt(GAME_NUMBER_OF_QUESTIONS_KEY, DEFAULT_NUMBER_OF_QUESTIONS).apply();
    }

    public QuizGameDifficulty getDifficulty() {
        String json_difficulty = prefs.getString(GAME_DIFFICULTY_KEY, "");
        return Json.getInstance().fromJson(json_difficulty, QuizGameDifficulty.class);
    }

    public void setDifficulty(QuizGameDifficulty difficulty) {
        prefs.edit()
                .putString(GAME_DIFFICULTY_KEY, Json.getInstance().toJson(difficulty))
                .putInt(GAME_NUMBER_OF_QUESTIONS_KEY, getNumberOfQuestionsForDifficulty(difficulty))
                .apply();
    }

    public static int getNumberOfQuestionsForDifficulty(QuizGameDifficulty difficulty) {
        return difficulty.getId() * QuizGameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public void saveLastGameDate() {
        final DateTime lastGameDate = new DateTime(new Date());
        prefs.edit().putString(LAST_GAME_DATE_KEY, lastGameDate.toString()).apply();
    }

    public DateTime getLastGameDate() {
        return DateTime.parse(prefs.getString(LAST_GAME_DATE_KEY, ""));
    }

    public void setOnlineTranslationsCheckPreference(boolean preference) {
        prefs.edit().putBoolean(ONLINE_TRANSLATIONS_CHECK_KEY, preference).apply();
    }

    public boolean getOnlineTranslationsCheckPreference() {
        return prefs.getBoolean(ONLINE_TRANSLATIONS_CHECK_KEY, false);
    }
}
