package com.matteoveroni.wordsremember.scene_settings.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.GameDifficulty;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.users.User;

import org.joda.time.DateTime;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Matteo Veroni
 */

public class Settings {

    public static final String TAG = TagGenerator.tag(Settings.class);

    @Inject
    ProfilesDBManager dbManager;

    private final SharedPreferences prefs;

    public static final String IS_STARTED_FOR_THE_FIRST_TIME_KEY = "is_started_for_the_first_time_key";
    public static boolean isAppStartedForTheFirstTime = true;

    public static final String USER_KEY = "user_key";
    public static final String USER_PROFILE_KEY = "user_profile_key";
    public static final String GAME_DIFFICULTY_KEY = "game_difficulty_key";
    public static final String QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME_KEY = "quiz_game_question_timer_total_time_key";
    public static final String QUIZ_GAME_QUESTION_TIMER_TICK_KEY = "quiz_game_question_timer_tick_key";
    public static final String GAME_NUMBER_OF_QUESTIONS_KEY = "game_number_of_questions_key";
    public static final String LAST_GAME_DATE_KEY = "last_game_date_key";
    public static final String ONLINE_TRANSLATION_SERVICE_KEY = "online_translation_service_key";

    public static final GameDifficulty DEFAULT_DIFFICULTY = GameDifficulty.EASY;
    public static final int DEFAULT_NUMBER_OF_QUESTIONS = getNumberOfQuestionsForDifficulty(DEFAULT_DIFFICULTY);
    public static final long DEFAULT_QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME = 10000;
    public static final long DEFAULT_QUIZ_GAME_TIMER_TICK = 1000;

    public class NoRegisteredUserException extends Exception {
    }

    public Settings(SharedPreferences prefs) {
        WordsRemember.getAppComponent().inject(this);
        this.prefs = prefs;
    }

    public Settings(SharedPreferences prefs, GameDifficulty difficulty) {
        this(prefs);
        setDifficulty(difficulty);
    }

    public boolean isAppStartedForTheFirstTime() {
        return prefs.getBoolean(IS_STARTED_FOR_THE_FIRST_TIME_KEY, isAppStartedForTheFirstTime);
    }

    public void setAppStartedForTheFirstTime(boolean value) {
        isAppStartedForTheFirstTime = value;
        prefs.edit().putBoolean(IS_STARTED_FOR_THE_FIRST_TIME_KEY, value).apply();
    }

    public void saveUser(User user) {
        if (user != null) prefs.edit().putString(USER_KEY, Json.getInstance().toJson(user)).apply();
    }

    public User getUser() throws NoRegisteredUserException {
        String json_user = prefs.getString(USER_KEY, "");
        if (json_user.trim().isEmpty()) {
            throw new NoRegisteredUserException();
        } else {
            return Json.getInstance().fromJson(json_user, User.class);
        }
    }

    public void setUserProfile(UserProfile userProfile) {
        prefs.edit().putString(USER_PROFILE_KEY, Json.getInstance().toJson(userProfile, UserProfile.class)).apply();
        dbManager.setUserProfileInUse(userProfile);
        Log.d(TAG, "Switch to user profile => " + userProfile.getName());
    }

    public UserProfile getUserProfile() {
        String json_userProfile = prefs.getString(USER_PROFILE_KEY, "");
        if (json_userProfile.trim().isEmpty()) {
            return UserProfile.USER_PROFILES;
        } else {
            return Json.getInstance().fromJson(json_userProfile, UserProfile.class);
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

    public GameDifficulty getDifficulty() {
        String json_difficulty = prefs.getString(GAME_DIFFICULTY_KEY, Json.getInstance().toJson(DEFAULT_DIFFICULTY));
        return Json.getInstance().fromJson(json_difficulty, GameDifficulty.class);
    }

    public void setDifficulty(GameDifficulty difficulty) {
        prefs.edit()
                .putString(GAME_DIFFICULTY_KEY, Json.getInstance().toJson(difficulty))
                .putInt(GAME_NUMBER_OF_QUESTIONS_KEY, getNumberOfQuestionsForDifficulty(difficulty))
                .apply();
    }

    public long getQuizGameQuestionTimerTotalTime() {
        return prefs.getLong(QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME_KEY, DEFAULT_QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME);
    }

    public void setQuizGameQuestionTimerTotalTime(int totalTime) {
        prefs.edit().putLong(QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME_KEY, totalTime).apply();
    }

    public long getQuizGameQuestionTimerTick() {
        return prefs.getLong(QUIZ_GAME_QUESTION_TIMER_TICK_KEY, DEFAULT_QUIZ_GAME_TIMER_TICK);
    }

    public void setQuizGameQuestionTimerTick(int tick) {
        prefs.edit().putLong(QUIZ_GAME_QUESTION_TIMER_TICK_KEY, tick).apply();
    }

    public static int getNumberOfQuestionsForDifficulty(GameDifficulty difficulty) {
        return difficulty.getId() * GameDifficulty.COMPLEXITY_MULTIPLIER;
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
