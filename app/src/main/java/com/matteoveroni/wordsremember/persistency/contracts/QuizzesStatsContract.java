package com.matteoveroni.wordsremember.persistency.contracts;

import android.net.Uri;
import android.provider.BaseColumns;

import com.matteoveroni.wordsremember.persistency.providers.dictionary.DictionaryProvider;
import com.matteoveroni.wordsremember.persistency.providers.statistics.StatisticsProvider;

/**
 * @author Matteo Veroni
 */

public class QuizzesStatsContract {
    public static final String NAME = DatesContract.Schema.TABLE_NAME;
    public static final String CURRENT_DATE_SQLITE = "CURRENT_DATE";

    public static final Uri DICTIONARY_CONTENT_URI = Uri.parse(
            DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    public static final Uri STATISTICS_CONTENT_URI = Uri.parse(
            StatisticsProvider.SCHEME + StatisticsProvider.CONTENT_AUTHORITY + "/" + NAME
    );

    public static final String DICTIONARY_MYME_TYPE_CONTENT_ITEM = DICTIONARY_CONTENT_URI + ".item";
    public static final String DICTIONARY_MYME_TYPE_CONTENT_DIR = DICTIONARY_CONTENT_URI + ".dir";
    public static final String STATISTICS_MYME_TYPE_CONTENT_ITEM = STATISTICS_CONTENT_URI + ".item";
    public static final String STATISTICS_MYME_TYPE_CONTENT_DIR = STATISTICS_CONTENT_URI + ".dir";

    public static final class Schema implements BaseColumns {
        public static final String TABLE_NAME = "QuizzesStats";

        public static final String COL_ID = _ID;
        public static final String COL_NUM_QUESTIONS = "NumbOfQuestions";
        public static final String COL_NUM_CORRECT_ANSWERS = "NumbOfCorrectAnswers";
        public static final String COL_NUM_WRONG_ANSWERS = "NumbOfWrongAnswers";
        public static final String COL_TOT_RESPONSE_TIME = "TotResponseTime";
        public static final String COL_AVG_RESPONSE_TIME = "AvgResponseTime";
        public static final String COL_DATE_TIME = "DateTime";

        public static final String TABLE_DOT_COL_ID = TABLE_NAME + "." + COL_ID;
        public static final String TABLE_DOT_COL_NUM_QUESTIONS = TABLE_NAME + "." + COL_NUM_QUESTIONS;
        public static final String TABLE_DOT_COL_NUM_CORRECT_ANSWERS = TABLE_NAME + "." + COL_NUM_CORRECT_ANSWERS;
        public static final String TABLE_DOT_COL_NUM_WRONG_ANSWERS = TABLE_NAME + "." + COL_NUM_WRONG_ANSWERS;
        public static final String TABLE_DOT_COL_TOT_RESPONSE_TIME = TABLE_NAME + "." + COL_TOT_RESPONSE_TIME;
        public static final String TABLE_DOT_COL_AVG_RESPONSE_TIME = TABLE_NAME + "." + COL_AVG_RESPONSE_TIME;
        public static final String TABLE_DOT_COL_DATE_TIME = TABLE_NAME + "." + COL_DATE_TIME;

        public static final String[] ALL_COLUMNS =
                {
                        TABLE_DOT_COL_ID,
                        TABLE_DOT_COL_NUM_QUESTIONS,
                        TABLE_DOT_COL_NUM_CORRECT_ANSWERS,
                        TABLE_DOT_COL_NUM_WRONG_ANSWERS,
                        TABLE_DOT_COL_TOT_RESPONSE_TIME,
                        TABLE_DOT_COL_AVG_RESPONSE_TIME,
                        TABLE_DOT_COL_DATE_TIME
                };
    }

    public static final class Query {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + QuizzesStatsContract.Schema.TABLE_NAME
                + "("
                + QuizzesStatsContract.Schema.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuizzesStatsContract.Schema.COL_NUM_QUESTIONS + " INTEGER NOT NULL, "
                + QuizzesStatsContract.Schema.COL_NUM_CORRECT_ANSWERS + " INTEGER, "
                + QuizzesStatsContract.Schema.COL_NUM_WRONG_ANSWERS + " INTEGER, "
                + QuizzesStatsContract.Schema.COL_TOT_RESPONSE_TIME + " INTEGER, "
                + QuizzesStatsContract.Schema.COL_AVG_RESPONSE_TIME + " INTEGER, "
                + QuizzesStatsContract.Schema.COL_DATE_TIME + " TEXT NOT NULL, "
                + "CONSTRAINT UQ_Date UNIQUE (" + QuizzesStatsContract.Schema.COL_NUM_QUESTIONS + ")"
                + ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + QuizzesStatsContract.Schema.TABLE_NAME;
    }
}
