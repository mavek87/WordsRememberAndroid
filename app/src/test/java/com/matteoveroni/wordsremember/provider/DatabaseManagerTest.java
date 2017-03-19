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

    private ShadowApplication app = Shadows.shadowOf(RuntimeEnvironment.application);
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
        dbManager = DatabaseManager.getInstance(app.getApplicationContext());
        assertNotNull("DbManager should be created before each test", dbManager);

        final SQLiteDatabase database = dbManager.getReadableDatabase();
        assertNotNull("Database should be created before each test", database);
        database.close();

        queryResults = null;
    }

    @After
    public void tearDown() {
        if (dbManager != null) {
            dbManager.close();
        }
        if (queryResults != null) {
            queryResults.close();
        }
        values.clear();
    }

    @Test
    public void test_AfterDbManagerIsCreated_DbNameShouldBeSet() {
        assertEquals("DbManager name should be equal to expected name", DATABASE_NAME, dbManager.getDatabaseName());
    }

    @Test
    public void test_AfterDbManagerCreation_TableVocables_ShouldBeCreatedAndEmpty() {
        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLES);
    }

    @Test
    public void test_AfterDbManagerIsCreated_TableTranslations_ShouldBeCreatedAndEmpty() {
        checkIfTableIsCreatedAndEmpty(TABLE_TRANSLATIONS);
    }

    @Test
    public void test_AfterDbManagerIsCreated_TableVocablesTranslations_ShouldBeCreatedAndEmpty() {
        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLESTRANSLATIONS);
    }

    private void checkIfTableIsCreatedAndEmpty(Table table) {
        queryResults = getQueryResultsFromTable(table);
        assertTrue(table.getName() + " QueryResults shouldn't contain any query result", queryResults.getCount() == 0);
    }

    @Test(expected = SQLiteException.class)
    public void test_WhenQueryExecutedUsingInvalidTable_SQliteExceptionShouldBeThrown() {
        queryResults = dbManager.getReadableDatabase().query(
                INVALID_TABLE,
                VocablesContract.Schema.ALL_COLUMNS,
                null, null, null, null, null, null
        );
    }

    @Test(expected = SQLiteException.class)
    public void test_WhenQueryExecutedUsingInvalidColumn_SQliteExceptionShouldBeThrown() {
        queryResults = dbManager.getReadableDatabase().query(
                VocablesContract.Schema.TABLE_NAME,
                INVALID_COLUMN,
                null, null, null, null, null, null
        );
    }

    @Test
    public void test_InsertValidVocableSucceed() {
        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);
        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
        assertTrue("After inserting valid vocable table should contains one value", queryResults.getCount() == 1);
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
    public void test_DeleteExistingVocableSucceed() {
        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);

        final int rowDeleted = dbManager.getWritableDatabase().delete(
                VocablesContract.Schema.TABLE_NAME,
                VocablesContract.Schema.COLUMN_ID + "=" + idOfDataInserted,
                null
        );
        assertEquals("Should delete only one row", 1, rowDeleted);

        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLES);
    }

    @Test
    public void test_UpdateExistingVocableSucceed() {
        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);

        final String UPDATED_VOCABLE_NAME = "UpdatedVocableName";
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, UPDATED_VOCABLE_NAME);

        final int rowsUpdated = dbManager.getWritableDatabase().update(
                VocablesContract.Schema.TABLE_NAME,
                values,
                VocablesContract.Schema.COLUMN_ID + "=" + idOfDataInserted, null);
        assertEquals("Rows updated should be only one", 1, rowsUpdated);

        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
        assertTrue("QueryResults must return just one vocable", queryResults.getCount() == 1);

        queryResults.moveToFirst();
        assertEquals("The only existing vocable should be updated like expected",
                UPDATED_VOCABLE_NAME,
                queryResults.getString(queryResults.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE))
        );
    }

    @Test
    public void test_resetDatabase_ShouldClearAllTables() {
        insertValidRecordInTable(TABLE_VOCABLES);
        insertValidRecordInTable(TABLE_TRANSLATIONS);
        insertValidRecordInTable(TABLE_VOCABLESTRANSLATIONS);

        dbManager.resetDatabase();

        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLES);
        checkIfTableIsCreatedAndEmpty(TABLE_TRANSLATIONS);
        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLESTRANSLATIONS);
    }

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

    private class Table {
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
