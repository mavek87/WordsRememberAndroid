package com.matteoveroni.wordsremember.settings.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Matteo Veroni
 */

public class Settings {

    private final SharedPreferences preferences;

    private int numberOfQuestions;

    public static final String GAME_DIFFICULTY_KEY = "game_difficulty_key";
    public static final String LAST_GAME_DATE_KEY = "last_game_date_key";

    public Settings(SharedPreferences preferences) {
        this.preferences = preferences;
        if (preferences.contains(GAME_DIFFICULTY_KEY)) {
            this.numberOfQuestions = getDifficulty().getId() * QuizGameDifficulty.COMPLEXITY_MULTIPLIER;
        }
    }

    public Settings(SharedPreferences preferences, QuizGameDifficulty difficulty) {
        this.preferences = preferences;
        setDifficulty(difficulty);
    }

    public QuizGameDifficulty getDifficulty() {
        String json_difficulty = preferences.getString(GAME_DIFFICULTY_KEY, "");
        QuizGameDifficulty difficulty = Json.getInstance().fromJson(json_difficulty, QuizGameDifficulty.class);
        return difficulty;
    }

    public void setDifficulty(QuizGameDifficulty difficulty) {
        preferences
                .edit()
                .putString(GAME_DIFFICULTY_KEY, Json.getInstance().toJson(difficulty))
                .apply();

        this.numberOfQuestions = difficulty.getId() * QuizGameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int number) {
        if (number < 0)
            throw new IllegalArgumentException("Number of questions cannot be negative");

        numberOfQuestions = number;
    }

    public void saveLastGameDate() {
        final DateTime lastGameDate = new DateTime(new Date());
        preferences
                .edit()
                .putString(LAST_GAME_DATE_KEY, lastGameDate.toString())
                .apply();
    }

    public DateTime getLastGameDate() throws Exception {
        return DateTime.parse(preferences.getString(LAST_GAME_DATE_KEY, ""));
//        return DateManager.DATE_FORMATTER.parseDateTime(preferences.getString(LAST_GAME_DATE_KEY, ""));
    }
}
