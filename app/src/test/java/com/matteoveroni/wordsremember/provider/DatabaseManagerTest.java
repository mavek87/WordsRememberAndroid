package com.matteoveroni.wordsremember.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

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

    static final String DICTIONARY_DATABASE_NAME = DatabaseManager.DB_NAME;

    static final String INVALID_TABLE = "invalid_table";
    static final String[] INVALID_COLUMN = {"invalid_column"};

    static final String VALID_VOCABLE_NAME = "test_vocable_123";

    DatabaseManager dbManager = null;
    Cursor cursor = null;
    ContentValues values = new ContentValues();

    @Before
    public void setUp() {
        assertNull(dbManager);
        assertNull(cursor);

        ShadowApplication application = Shadows.shadowOf(RuntimeEnvironment.application);
        dbManager = DatabaseManager.getInstance(application.getApplicationContext());

        assertNotNull(dbManager);
    }

    @After
    public void tearDown() {
        values.clear();
        if (dbManager != null) {
            dbManager.close();
            dbManager = null;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Test
    public void when_db_manager_is_created_db_name_should_be_set() {
        assertNotNull("dbManager must not be null after setUp", dbManager);
        assertEquals("dbManager name must be equal to expected name", DICTIONARY_DATABASE_NAME, dbManager.getDatabaseName());
    }

    @Test
    public void when_db_manager_is_created_dictionary_table_should_be_created() {
        final SQLiteDatabase db = dbManager.getReadableDatabase();
        cursor = db.query(VocablesContract.Schema.TABLE_NAME, VocablesContract.Schema.ALL_COLUMNS, "", null, null, null, null, null);
        assertNotNull("cursor must not be null after selection query on existing table", cursor);
        assertTrue("cursor mustn't contain any query result", cursor.getCount() == 0);
    }

    @Test(expected = SQLiteException.class)
    public void sqlite_exception_should_be_thrown_when_query_executed_on_invalid_table() {
        try {
            final SQLiteDatabase db = dbManager.getReadableDatabase();
            cursor = db.query(INVALID_TABLE, VocablesContract.Schema.ALL_COLUMNS, "", null, null, null, null, null);
        } catch (SQLiteException ex) {
            throw new SQLiteException();
        }
    }

    @Test(expected = SQLiteException.class)
    public void sqlite_exception_should_be_thrown_when_query_executed_using_invalid_column() {
        try {
            final SQLiteDatabase db = dbManager.getReadableDatabase();
            cursor = db.query(VocablesContract.Schema.TABLE_NAME, INVALID_COLUMN, "", null, null, null, null, null);
        } catch (SQLiteException ex) {
            throw new SQLiteException();
        }
    }

    @Test
    public void select_existing_vocable_succeed() {
        insertValidVocable();
        final SQLiteDatabase db = dbManager.getReadableDatabase();

        cursor = db.query(
                VocablesContract.Schema.TABLE_NAME,
                VocablesContract.Schema.ALL_COLUMNS,
                null, null, null, null, null
        );

        assertTrue("query must return just one value", cursor.getCount() == 1);
        assertTrue("move to the first retrieved vocable position", cursor.moveToFirst());
        assertEquals("the first valid_id must be equal to one",
                1,
                cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_ID))
        );
        assertEquals("the inserted vocable name must match with the expected value",
                VALID_VOCABLE_NAME,
                cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE))
        );
    }

    @Test
    public void delete_existing_vocable_succeed() {
        insertValidVocable();

        final SQLiteDatabase db = dbManager.getWritableDatabase();

        int rowDeleted = db.delete(
                VocablesContract.Schema.TABLE_NAME,
                VocablesContract.Schema.COLUMN_ID + " = " + 1,
                null
        );
        assertEquals("should delete only one row", 1, rowDeleted);

        cursor = db.query(
                VocablesContract.Schema.TABLE_NAME,
                VocablesContract.Schema.ALL_COLUMNS,
                null, null, null, null, null
        );
        assertTrue("query must return no values because the vocable should be deleted", cursor.getCount() == 0);
        cursor.close();
    }

    @Test
    public void update_existing_vocable_succeed() {
        insertValidVocable();

        final SQLiteDatabase db = dbManager.getWritableDatabase();
        final ContentValues updatedValues = new ContentValues();
        final String UPDATED_VOCABLE_NAME = "UpdatedVocable";

        updatedValues.put(VocablesContract.Schema.COLUMN_VOCABLE, UPDATED_VOCABLE_NAME);

        final int rowsUpdated = db.update(VocablesContract.Schema.TABLE_NAME, updatedValues, VocablesContract.Schema.COLUMN_ID + " = " + 1, null);
        assertEquals("rows updated should be only one", 1, rowsUpdated);

        cursor = db.query(
                VocablesContract.Schema.TABLE_NAME,
                VocablesContract.Schema.ALL_COLUMNS,
                null, null, null, null, null
        );
        assertTrue("query must return just one vocable", cursor.getCount() == 1);

        cursor.moveToFirst();
        assertEquals(
                "the only vocable should be updated like expected",
                UPDATED_VOCABLE_NAME, cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE))
        );
    }

    private void insertValidVocable() {
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, VALID_VOCABLE_NAME);
        final SQLiteDatabase db = dbManager.getWritableDatabase();
        long idOfVocableInserted = db.insert(VocablesContract.Schema.TABLE_NAME, null, values);
        assertEquals("id generated from insert is equal to one", 1, idOfVocableInserted);
        dbManager.close();
    }
}
