package com.matteoveroni.wordsremember.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Matteo Veroni
 */

/**
 * Useful resources:
 * <p>
 * https://github.com/jameskbride/grocery-reminder/blob/master/app/src/test/java/com/groceryreminder/data/ReminderDBHelperTest.java
 * jameskbride.com/2016/02/13/android-tdd-series-test-driving-data-part-1-sqliteopenhelper.html
 * </p>
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DatabaseManagerTest {

    private DatabaseManager dbManager;
    private Cursor queryResults;
    private ContentValues values = new ContentValues();

    private static final String DATABASE_NAME = DatabaseManager.DB_NAME;

    private final Table TABLE_VOCABLES = new Table(VocablesContract.Schema.TABLE_NAME, VocablesContract.Schema.ALL_COLUMNS);
    private final Table TABLE_TRANSLATIONS = new Table(TranslationsContract.Schema.TABLE_NAME, TranslationsContract.Schema.ALL_COLUMNS);
    private final Table TABLE_VOCABLESTRANSLATIONS = new Table(VocablesTranslationsContract.Schema.TABLE_NAME, VocablesTranslationsContract.Schema.ALL_COLUMNS);

    private static final String INVALID_TABLE = "invalid_table";
    private static final String[] INVALID_COLUMN = {"invalid_column"};

    private static final String VALID_VOCABLE_NAME = "test_vocable_1";
    private static final String VALID_TRANSLATION_NAME = "test_translation_1";

    @Before
    public void setUp() {
        dbManager = null;
        queryResults = null;

        ShadowApplication application = Shadows.shadowOf(RuntimeEnvironment.application);
        dbManager = DatabaseManager.getInstance(application.getApplicationContext());

        assertNotNull("DbManager should not be null after setUp", dbManager);
    }

    @After
    public void tearDown() {
        values.clear();
        if (dbManager != null) {
            dbManager.close();
        }
        if (queryResults != null) {
            queryResults.close();
        }
    }

    @Test
    public void WHEN_DB_MANAGER_IS_CREATED_DB_NAME_SHOULD_BE_SET() {
        assertEquals("DbManager name should be equal to expected name", DATABASE_NAME, dbManager.getDatabaseName());
    }

    @Test
    public void WHEN_DB_MANAGER_IS_CREATED_VOCABLES_TABLE_SHOULD_BE_CREATED() {
        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
        assertNotNull(TABLE_VOCABLES.getName() + "QueryResults shouldn't be null after selection query on existing table", queryResults);
        assertTrue(TABLE_VOCABLES.getName() + " QueryResults shouldn't contains any query result", queryResults.getCount() == 0);
    }

    @Test
    public void WHEN_DB_MANAGER_IS_CREATED_TRANSLATIONS_TABLE_SHOULD_BE_CREATED() {
        queryResults = getQueryResultsFromTable(TABLE_TRANSLATIONS);
        assertNotNull(TABLE_VOCABLES.getName() + "QueryResults shouldn't be null after selection query on existing table", queryResults);
        assertTrue(TABLE_VOCABLES.getName() + " QueryResults shouldn't contains any query result", queryResults.getCount() == 0);
    }

    @Test
    public void WHEN_DB_MANAGER_IS_CREATED_VOCABLESTRANSLATIONS_TABLE_SHOULD_BE_CREATED() {
        queryResults = getQueryResultsFromTable(TABLE_VOCABLESTRANSLATIONS);
        assertNotNull(TABLE_VOCABLES.getName() + "QueryResults shouldn't be null after selection query on existing table", queryResults);
        assertTrue(TABLE_VOCABLES.getName() + " QueryResults shouldn't contains any query result", queryResults.getCount() == 0);
    }

    @Test(expected = SQLiteException.class)
    public void SQLITE_EXCEPTION_SHOULD_BE_THROWN_WHEN_QUERY_EXECUTED_ON_INVALID_TABLE() {
        queryResults = dbManager.getReadableDatabase().query(
                INVALID_TABLE,
                VocablesContract.Schema.ALL_COLUMNS,
                null, null, null, null, null, null
        );
    }

    @Test(expected = SQLiteException.class)
    public void SQLITE_EXCEPTION_SHOULD_BE_THROWN_WHEN_QUERY_EXECUTED_ON_INVALID_COLUMN() {
        queryResults = dbManager.getReadableDatabase().query(
                VocablesContract.Schema.TABLE_NAME,
                INVALID_COLUMN,
                null, null, null, null, null, null
        );
    }

    @Test
    public void SELECT_INSERTED_VOCABLE_SUCCEED() {
        long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);

        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);

        assertTrue("QueryResults should return just one value", queryResults.getCount() == 1);
        assertTrue("Should move to the first retrieved vocable position", queryResults.moveToFirst());
        assertEquals("The id of the only record existing should be equal to the id of data inserted",
                idOfDataInserted,
                queryResults.getLong(queryResults.getColumnIndex(VocablesContract.Schema.COLUMN_ID))
        );
        assertEquals("The inserted vocable name should match with the expected value",
                VALID_VOCABLE_NAME,
                queryResults.getString(queryResults.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE))
        );
    }

    @Test
    public void DELETE_EXISTING_VOCABLE_SUCCEED() {
        long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);
        assertEquals("Id generated from insert should be equal to one", 1, idOfDataInserted);

        int rowDeleted = dbManager.getWritableDatabase().delete(
                VocablesContract.Schema.TABLE_NAME,
                VocablesContract.Schema.COLUMN_ID + " = " + idOfDataInserted,
                null
        );
        assertEquals("Should delete only one row", 1, rowDeleted);

        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
        assertTrue("QueryResults should return no values because the vocable should be deleted", queryResults.getCount() == 0);
    }

    @Test
    public void UPDATE_EXISTING_VOCABLE_SUCCEED() {
        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);
        final String UPDATED_VOCABLE_NAME = "UpdatedVocableName";
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, UPDATED_VOCABLE_NAME);

        final int rowsUpdated = dbManager.getWritableDatabase().update(
                VocablesContract.Schema.TABLE_NAME,
                values,
                VocablesContract.Schema.COLUMN_ID + " = " + idOfDataInserted, null);
        assertEquals("Rows updated should be only one", 1, rowsUpdated);

        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
        assertTrue("QueryResults must return just one vocable", queryResults.getCount() == 1);

        queryResults.moveToFirst();
        assertEquals(
                "The only existing vocable should be updated like expected",
                UPDATED_VOCABLE_NAME, queryResults.getString(queryResults.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE))
        );
    }

    @Test
    public void RESET_DATABASE_SHOULD_CLEAR_ALL_THE_TABLES() {
        insertValidRecordInTable(TABLE_VOCABLES);
        insertValidRecordInTable(TABLE_TRANSLATIONS);
        insertValidRecordInTable(TABLE_VOCABLESTRANSLATIONS);

        dbManager.resetDatabase();

        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
        assertTrue(TABLE_VOCABLES.getName() + " QueryResults shouldn't contains any query result", queryResults.getCount() == 0);

        queryResults = getQueryResultsFromTable(TABLE_TRANSLATIONS);
        assertTrue(TABLE_VOCABLES.getName() + " QueryResults shouldn't contains any query result", queryResults.getCount() == 0);

        queryResults = getQueryResultsFromTable(TABLE_VOCABLESTRANSLATIONS);
        assertTrue(TABLE_VOCABLES.getName() + " QueryResults shouldn't contains any query result", queryResults.getCount() == 0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Cursor getQueryResultsFromTable(Table table) {
        return dbManager.getReadableDatabase().query(table.getName(), table.getColumns(), null, null, null, null, null, null);
    }

    private long insertValidRecordInTable(Table table) {
        switch (table.getName()) {
            case VocablesContract.Schema.TABLE_NAME:
                values.put(VocablesContract.Schema.COLUMN_VOCABLE, VALID_VOCABLE_NAME);
                break;
            case TranslationsContract.Schema.TABLE_NAME:
                values.put(TranslationsContract.Schema.COLUMN_TRANSLATION, VALID_TRANSLATION_NAME);
                break;
            case VocablesTranslationsContract.Schema.TABLE_NAME:
                values.put(VocablesTranslationsContract.Schema.COLUMN_VOCABLE_ID, 1);
                values.put(VocablesTranslationsContract.Schema.COLUMN_TRANSLATION_ID, 1);
                break;
            default:
                throw new RuntimeException("Error trying to insert new record in table. Table " + table.getName() + " unknown");
        }
        long idOfDataInserted = dbManager.getWritableDatabase().insert(table.getName(), null, values);
        dbManager.close();
        return idOfDataInserted;
    }

    class Table {
        private final String name;
        private final String[] columns;

        Table(String name, String[] columns) {
            this.name = name;
            this.columns = columns;
        }

        String getName() {
            return name;
        }

        String[] getColumns() {
            return columns;
        }
    }
}
