package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.QuizStatsContract;
import com.matteoveroni.wordsremember.persistency.ProfilesDBManager;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.quiz.Quiz;

import java.util.Date;

/**
 * @author Matteo Veroni
 */

public class StatisticsDAO {

    private final static String TAG = TagGenerator.tag(StatisticsDAO.class);
    private final ContentResolver contentResolver;

    public StatisticsDAO(Context context, ProfilesDBManager profilesDBManager) {
        this.contentResolver = context.getContentResolver();
    }

    public long saveQuizResults(Quiz quiz) {
        Uri uri = contentResolver.insert(QuizStatsContract.CONTENT_URI, quizToContentValue(quiz));

//        Cursor cursor = contentResolver.query(QuizStatsContract.CONTENT_URI, null, null, null, null);
//        int savedNumberOfQuestions = cursor.getInt(cursor.getColumnIndex(QuizStatsContract.Schema.COL_NUM_QUESTIONS));
//
//        Log.d(TAG, "Saved number of questions = " + savedNumberOfQuestions);

        return Long.valueOf(uri.getLastPathSegment());
    }

//
//    public int updateUserProfile(UserProfile oldUserProfile, UserProfile newUserProfile) throws Exception {
//        checkIfUserProfileIsValidOrThrowException(newUserProfile, new IllegalArgumentException("Invalid new user profile to is for the update"));
//
//        if (oldUserProfile.isInvalidProfile()) {
//            try {
//                saveUserProfile(newUserProfile);
//                return 1;
//            } catch (Exception ex) {
//                throw new IllegalArgumentException("Invalid old user profile to update and impossible to save new user profile");
//            }
//        }
//
//        if (newUserProfile.equals(oldUserProfile)) return 0;
//
//        profilesDBManager.updateDBForNewUserProfile(oldUserProfile, newUserProfile);
//
//        return contentResolver.update(
//                UserProfilesContract.CONTENT_URI,
//                userProfileToContentValues(newUserProfile),
//                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
//                new String[]{Long.toString(oldUserProfile.getId())}
//        );
//    }
//
//    public void deleteUserProfile(UserProfile userProfile) throws Exception {
//        checkIfUserProfileIsValidOrThrowException(userProfile, new IllegalArgumentException("Invalid user profile to delete"));
//
//        profilesDBManager.deleteUserProfileDB(userProfile);
//
//        contentResolver.delete(
//                UserProfilesContract.CONTENT_URI,
//                UserProfilesContract.Schema.TABLE_DOT_COL_ID + "=?",
//                new String[]{Long.toString(userProfile.getId())}
//        );
//    }

//    private void checkIfQuizIsValidOrThrowException(Quiz userProfile, Exception ex) throws Exception {
//        if (userProfile.isInvalidProfile())
//            throw ex;
//    }

    private ContentValues quizToContentValue(Quiz quiz) {
        final ContentValues values = new ContentValues();

        final int totalNumberOfQuestions = quiz.getTotalNumberOfQuestions();
        final int numberOfCorrectAnswers = quiz.getCorrectAnswers().size();
        final int numberOfWrongAnswers = quiz.getWrongAnswers().size();
        final long totalResponseTime = quiz.getTotalResponseTime();
        final long avgResponseTime = quiz.getAverageResponseTime();
        final String difficulty = quiz.getGameDifficulty().name();

        values.put(QuizStatsContract.Schema.COL_NUM_QUESTIONS, totalNumberOfQuestions);
        values.put(QuizStatsContract.Schema.COL_NUM_CORRECT_ANSWERS, numberOfCorrectAnswers);
        values.put(QuizStatsContract.Schema.COL_NUM_WRONG_ANSWERS, numberOfWrongAnswers);
        values.put(QuizStatsContract.Schema.COL_TOT_RESPONSE_TIME, totalResponseTime);
        values.put(QuizStatsContract.Schema.COL_AVG_RESPONSE_TIME, avgResponseTime);
        values.put(QuizStatsContract.Schema.COL_DATE_TIME, String.valueOf(new Date()));
        values.put(QuizStatsContract.Schema.COL_DIFFICULTY, difficulty);

        return values;
    }

//    public static Quiz cursorToQuiz(Cursor cursor) {
//        final int totalNumberOfQuestions = cursor.getInt(cursor.getColumnIndex(QuizStatsContract.Schema.COL_NUM_QUESTIONS));
//        final int numberOfCorrectAnswers = cursor.getInt(cursor.getColumnIndex(QuizStatsContract.Schema.COL_NUM_CORRECT_ANSWERS));
//        final int numberOfWrongAnswers = cursor.getInt(cursor.getColumnIndex(QuizStatsContract.Schema.COL_NUM_WRONG_ANSWERS));
//        final long totalResponseTime = cursor.getLong(cursor.getColumnIndex(QuizStatsContract.Schema.COL_TOT_RESPONSE_TIME));
//        final long avgResponseTime = cursor.getLong(cursor.getColumnIndex(QuizStatsContract.Schema.COL_AVG_RESPONSE_TIME));
//
//        GameDifficulty gameDifficulty;
//        String difficulty = cursor.getString(cursor.getColumnIndex(QuizStatsContract.Schema.COL_DIFFICULTY));
//
//        if (difficulty.equals(GameDifficulty.EASY.name())) {
//            gameDifficulty = GameDifficulty.EASY;
//        } else if (difficulty.equals(GameDifficulty.MEDIUM.name())) {
//            gameDifficulty = GameDifficulty.MEDIUM;
//        } else if (difficulty.equals(GameDifficulty.HARD.name())) {
//            gameDifficulty = GameDifficulty.HARD;
//        }
//
//        Quiz quiz = new Quiz(gameDifficulty);
//
//        quiz.setTotalNumberOfQuestions(totalNumberOfQuestions);
//        quiz.set
//
//        cursor.getString(cursor.getColumnIndex(QuizStatsContract.Schema.))
//        translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COL_ID)));
//
//        return translation;
//    }
}


