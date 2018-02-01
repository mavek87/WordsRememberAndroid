package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.statistics.StatisticsProvider;

/**
 * @author Matteo Veroni
 */

public class QuizStatsContract {
    public static final String NAME = Schema.TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse(
            StatisticsProvider.SCHEME + StatisticsProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    public static final String CONTENT_ITEM_MYME_TYPE = CONTENT_URI + ".item";
    public static final String CONTENT_DIR_MYME_TYPE = CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "QuizStats";

        public static final String COL_ID = _ID;
        public static final String COL_NUM_QUESTIONS = "NumbOfQuestions";
        public static final String COL_NUM_CORRECT_ANSWERS = "NumbOfCorrectAnswers";
        public static final String COL_NUM_WRONG_ANSWERS = "NumbOfWrongAnswers";
        public static final String COL_TOT_RESPONSE_TIME = "TotResponseTime";
        public static final String COL_AVG_RESPONSE_TIME = "AvgResponseTime";
        public static final String COL_DATE_TIME = "DateTime";
        public static final String COL_DIFFICULTY = "Difficulty";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + COL_ID;
        public static final String TABLE_DOT_COL_NUM_QUESTIONS = TABLE_NAME + "." + COL_NUM_QUESTIONS;
        public static final String TABLE_DOT_COL_NUM_CORRECT_ANSWERS = TABLE_NAME + "." + COL_NUM_CORRECT_ANSWERS;
        public static final String TABLE_DOT_COL_NUM_WRONG_ANSWERS = TABLE_NAME + "." + COL_NUM_WRONG_ANSWERS;
        public static final String TABLE_DOT_COL_TOT_RESPONSE_TIME = TABLE_NAME + "." + COL_TOT_RESPONSE_TIME;
        public static final String TABLE_DOT_COL_AVG_RESPONSE_TIME = TABLE_NAME + "." + COL_AVG_RESPONSE_TIME;
        public static final String TABLE_DOT_COL_DATE_TIME = TABLE_NAME + "." + COL_DATE_TIME;
        public static final String TABLE_DOT_COL_DIFFICULTY = TABLE_NAME + "." + COL_DIFFICULTY;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_NUM_QUESTIONS,
                        TABLE_DOT_COL_NUM_CORRECT_ANSWERS,
                        TABLE_DOT_COL_NUM_WRONG_ANSWERS,
                        TABLE_DOT_COL_TOT_RESPONSE_TIME,
                        TABLE_DOT_COL_AVG_RESPONSE_TIME,
                        TABLE_DOT_COL_DATE_TIME,
                        TABLE_DOT_COL_DIFFICULTY
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + QuizStatsContract.Schema.TABLE_NAME
                + "("
                + QuizStatsContract.Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuizStatsContract.Schema.COL_NUM_QUESTIONS + " INTEGER NOT NULL, "
                + QuizStatsContract.Schema.COL_NUM_CORRECT_ANSWERS + " INTEGER, "
                + QuizStatsContract.Schema.COL_NUM_WRONG_ANSWERS + " INTEGER, "
                + QuizStatsContract.Schema.COL_TOT_RESPONSE_TIME + " INTEGER, "
                + QuizStatsContract.Schema.COL_AVG_RESPONSE_TIME + " INTEGER, "
                + QuizStatsContract.Schema.COL_DATE_TIME + " TEXT NOT NULL, "
                + QuizStatsContract.Schema.COL_DIFFICULTY + " TEXT NOT NULL"
                + ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + QuizStatsContract.Schema.TABLE_NAME;
    }
}
