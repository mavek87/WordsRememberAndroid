package com.matteoveroni.wordsremember.scene_settings.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.game.GameDifficulty;
import com.matteoveroni.wordsremember.scene_settings.exceptions.NoRegisteredUserException;
import com.matteoveroni.wordsremember.scene_settings.exceptions.UnreadableUserInSettingsException;
import com.matteoveroni.wordsremember.scene_settings.exceptions.UserRegistrationLimitReachedException;
import com.matteoveroni.wordsremember.scene_userprofile.EmptyProfile;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;
import com.matteoveroni.wordsremember.users.User;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Matteo Veroni
 */

public class Settings {
    public static final String TAG = TagGenerator.tag(Settings.class);

    public static final GameDifficulty DEFAULT_DIFFICULTY = GameDifficulty.EASY;
    public static final int DEFAULT_NUMBER_OF_QUESTIONS = getNumberOfQuestionsForDifficulty(DEFAULT_DIFFICULTY);
    public static final long DEFAULT_QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME = 10000;
    public static final long DEFAULT_QUIZ_GAME_TIMER_TICK = 1000;
    private static final int MAX_NUMBER_OF_REGISTRABLE_USERS = 1;

    private final SharedPreferences prefs;

    public class Key {
        public static final String USER = "user_key";
        public static final String NUMBER_OF_REGISTERED_USERS = "number_of_registered_users_key";
        public static final String USER_PROFILE = "user_profile_key";
        public static final String GAME_DIFFICULTY = "game_difficulty_key";
        public static final String QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME = "quiz_game_question_timer_total_time_key";
        public static final String QUIZ_GAME_QUESTION_TIMER_TICK = "quiz_game_question_timer_tick_key";
        public static final String GAME_NUMBER_OF_QUESTIONS = "game_number_of_questions_key";
        public static final String LAST_GAME_DATE = "last_game_date_key";
        public static final String ONLINE_TRANSLATION_SERVICE = "online_translation_service_key";
    }

    public Settings(SharedPreferences prefs) {
        WordsRemember.getAppComponent().inject(this);
        this.prefs = prefs;
    }

    public Settings(SharedPreferences prefs, GameDifficulty difficulty) {
        this(prefs);
        setDifficulty(difficulty);
    }

    public int getNumberOfRegisteredUsers() {
        if (prefs.contains(Key.NUMBER_OF_REGISTERED_USERS)) {
            return prefs.getInt(Key.NUMBER_OF_REGISTERED_USERS, 1);
        } else {
            return 0;
        }
    }

    public void registerUser(User user) throws UserRegistrationLimitReachedException {
        int numberOfRegisteredUsers = getNumberOfRegisteredUsers();
        if (numberOfRegisteredUsers == 0) {
            prefs.edit().putString(Key.USER, Json.getInstance().toJson(user)).apply();
            prefs.edit().putInt(Key.NUMBER_OF_REGISTERED_USERS, ++numberOfRegisteredUsers).apply();
        } else {
            throw new UserRegistrationLimitReachedException();
        }
    }

    public User getRegisteredUser() throws NoRegisteredUserException, UnreadableUserInSettingsException {
        int numberOfRegisteredUsers = getNumberOfRegisteredUsers();
        if (numberOfRegisteredUsers == 0) {
            throw new NoRegisteredUserException();
        } else {
            String json_user = prefs.getString(Key.USER, "");
            if (json_user.trim().isEmpty()) {
                throw new UnreadableUserInSettingsException();
            } else {
                return Json.getInstance().fromJson(json_user, User.class);
            }
        }
    }

    public Profile getUserProfile() {
        String json_userProfile = prefs.getString(Key.USER_PROFILE, "");
        //TODO: remove user_profiles
        if (json_userProfile.trim().isEmpty()) {
            return new EmptyProfile();
        } else {
            return Json.getInstance().fromJson(json_userProfile, Profile.class);
        }
    }


    public void saveUserProfile(Profile userProfile) {
        prefs.edit().putString(Key.USER_PROFILE, Json.getInstance().toJson(userProfile, Profile.class)).apply();
    }

    public boolean containsUserProfile() {
        return containsKey(Key.USER_PROFILE);
    }

    public int getDefaultNumberOfQuestions() {
        return prefs.getInt(Key.GAME_NUMBER_OF_QUESTIONS, DEFAULT_NUMBER_OF_QUESTIONS);
    }

    public void setDefaultNumberOfQuestions(int number) {
        if (number < 0)
            throw new IllegalArgumentException("Number of questions can\'t be negative");

        prefs.edit().putInt(Key.GAME_NUMBER_OF_QUESTIONS, DEFAULT_NUMBER_OF_QUESTIONS).apply();
    }

    public GameDifficulty getDifficulty() {
        String json_difficulty = prefs.getString(Key.GAME_DIFFICULTY, Json.getInstance().toJson(DEFAULT_DIFFICULTY));
        return Json.getInstance().fromJson(json_difficulty, GameDifficulty.class);
    }

    public void setDifficulty(GameDifficulty difficulty) {
        prefs.edit()
                .putString(Key.GAME_DIFFICULTY, Json.getInstance().toJson(difficulty))
                .putInt(Key.GAME_NUMBER_OF_QUESTIONS, getNumberOfQuestionsForDifficulty(difficulty))
                .apply();
    }

    public static int getNumberOfQuestionsForDifficulty(GameDifficulty difficulty) {
        return difficulty.getId() * GameDifficulty.COMPLEXITY_MULTIPLIER;
    }

    public long getQuizGameQuestionTimerTotalTime() {
        return prefs.getLong(Key.QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME, DEFAULT_QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME);
    }

    public void setQuizGameQuestionTimerTotalTime(int totalTime) {
        prefs.edit().putLong(Key.QUIZ_GAME_QUESTION_TIMER_TOTAL_TIME, totalTime).apply();
    }

    public long getQuizGameQuestionTimerTick() {
        return prefs.getLong(Key.QUIZ_GAME_QUESTION_TIMER_TICK, DEFAULT_QUIZ_GAME_TIMER_TICK);
    }

    public void setQuizGameQuestionTimerTick(int tick) {
        prefs.edit().putLong(Key.QUIZ_GAME_QUESTION_TIMER_TICK, tick).apply();
    }

    public DateTime getLastGameDate() {
        String str_dateTime = prefs.getString(Key.LAST_GAME_DATE, "");
        return (str_dateTime.trim().isEmpty()) ? null : DateTime.parse(str_dateTime);
    }

    public void saveLastGameDate() {
        DateTime lastGameDate = new DateTime(new Date());
        prefs.edit().putString(Key.LAST_GAME_DATE, lastGameDate.toString()).apply();
    }

    public boolean isOnlineTranslationServiceEnabled() {
        return prefs.getBoolean(Key.ONLINE_TRANSLATION_SERVICE, false);
    }

    public void setOnlineTranslationServiceEnabled(boolean preference) {
        prefs.edit().putBoolean(Key.ONLINE_TRANSLATION_SERVICE, preference).apply();
    }

    private boolean containsKey(String keyCode) {
        return prefs.contains(keyCode);
    }
}
