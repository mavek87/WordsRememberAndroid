package com.matteoveroni.wordsremember.settings.model;

import android.content.SharedPreferences;

import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.quizgame.business_logic.QuizGameDifficulty;
import com.matteoveroni.wordsremember.users.User;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Matteo Veroni
 */

public class Settings {

    private final SharedPreferences prefs;

    public static final String USER_KEY = "user_key";
    public static final String GAME_DIFFICULTY_KEY = "game_difficulty_key";
    public static final String QUIZ_GAME_TIMER_TOTAL_TIME_KEY = "quiz_game_timer_total_time_key";
    public static final String QUIZ_GAME_TIMER_TICK_KEY = "quiz_game_timer_tick_key";
    public static final String GAME_NUMBER_OF_QUESTIONS_KEY = "game_number_of_questions_key";
    public static final String LAST_GAME_DATE_KEY = "last_game_date_key";
    public static final String ONLINE_TRANSLATION_SERVICE_KEY = "online_translation_service_key";

    public static final QuizGameDifficulty DEFAULT_DIFFICULTY = QuizGameDifficulty.EASY;
    public static final int DEFAULT_NUMBER_OF_QUESTIONS = getNumberOfQuestionsForDifficulty(DEFAULT_DIFFICULTY);
    public static final long DEFAULT_QUIZ_GAME_TIMER_TOTAL_TIME = 10000;
    public static final long DEFAULT_QUIZ_GAME_TIMER_TICK = 1000;

    public class NoRegisteredUserException extends Exception {
    }

    public Settings(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public Settings(SharedPreferences prefs, QuizGameDifficulty difficulty) {
        this.prefs = prefs;
        setDifficulty(difficulty);
    }

    public void saveUser(User user) {
        if (user != null) prefs.edit().putString(USER_KEY, Json.getInstance().toJson(user)).apply();
    }

    public User getUser() throws NoRegisteredUserException {
        String json_user = prefs.getString(USER_KEY, "");

        if (json_user.trim().isEmpty()) {
            throw new NoRegisteredUserException();
        } else {
            User user = Json.getInstance().fromJson(json_user, User.class);
            return user;
        }
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
        String json_difficulty = prefs.getString(GAME_DIFFICULTY_KEY, Json.getInstance().toJson(DEFAULT_DIFFICULTY));
        return Json.getInstance().fromJson(json_difficulty, QuizGameDifficulty.class);
    }

    public void setDifficulty(QuizGameDifficulty difficulty) {
        prefs.edit()
                .putString(GAME_DIFFICULTY_KEY, Json.getInstance().toJson(difficulty))
                .putInt(GAME_NUMBER_OF_QUESTIONS_KEY, getNumberOfQuestionsForDifficulty(difficulty))
                .apply();
    }

    public long getQuizGameTimerTotalTime() {
        return prefs.getLong(QUIZ_GAME_TIMER_TOTAL_TIME_KEY, DEFAULT_QUIZ_GAME_TIMER_TOTAL_TIME);
    }

    public void setQuizGameTimerTotalTime(int totalTime) {
        prefs.edit().putLong(QUIZ_GAME_TIMER_TOTAL_TIME_KEY, totalTime).apply();
    }

    public long getQuizGameTimerTick() {
        return prefs.getLong(QUIZ_GAME_TIMER_TICK_KEY, DEFAULT_QUIZ_GAME_TIMER_TICK);
    }

    public void setQuizGameTimerTick(int tick) {
        prefs.edit().putLong(QUIZ_GAME_TIMER_TICK_KEY, tick).apply();
    }

    public static int getNumberOfQuestionsForDifficulty(QuizGameDifficulty difficulty) {
        return difficulty.getId() * QuizGameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public void saveLastGameDate() {
        final DateTime lastGameDate = new DateTime(new Date());
        prefs.edit().putString(LAST_GAME_DATE_KEY, lastGameDate.toString()).apply();
    }

    public DateTime getLastGameDate() {
        String str_dateTime = prefs.getString(LAST_GAME_DATE_KEY, "");
        return (str_dateTime.trim().isEmpty()) ? null : DateTime.parse(str_dateTime);
    }

    public void setOnlineTranslationServiceEnabled(boolean preference) {
        prefs.edit().putBoolean(ONLINE_TRANSLATION_SERVICE_KEY, preference).apply();
    }

    public boolean isOnlineTranslationServiceEnabled() {
        return prefs.getBoolean(ONLINE_TRANSLATION_SERVICE_KEY, false);
    }

}
