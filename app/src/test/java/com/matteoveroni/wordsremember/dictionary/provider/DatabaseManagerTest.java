package com.matteoveroni.wordsremember.dictionary.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

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

    private static final String DICTIONARY_DATABASE_NAME = DatabaseManager.NAME;

    public static final String INVALID_TABLE = "invalid_table";
    public static final String[] INVALID_COLUMN = {"invalid_column"};

    public static final String VALID_VOCABLE_NAME = "test_vocable_123";

    private DatabaseManager dbManager = null;

    @Before
    public void setUp() {
        ShadowApplication application = Shadows.shadowOf(RuntimeEnvironment.application);
        dbManager = DatabaseManager.getInstance(application.getApplicationContext());
    }

    @After
    public void tearDown() {
        if (dbManager != null) {
            dbManager.close();
        }
    }

    @Test
    public void testWhenDBManagerIsCreatedThenTheDBNameShouldBeSet() {
        assertNotNull("dbManager must be not null after setUp", dbManager);
        assertEquals("dbManager name must be equal to expected name", DICTIONARY_DATABASE_NAME, dbManager.getDatabaseName());
    }

    @Test
    public void testWhenTheDBManagerIsCreatedThenTheDictionaryTableShouldBeCreated() {
        Cursor cursor;
        final SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(DictionaryContract.Schema.TABLE_NAME, DictionaryContract.Schema.ALL_COLUMNS, "", null, null, null, null, null);
        assertNotNull("cursor must be not null after select_query_on_empty_db_return_zero_results query on select_query_on_empty_db_return_zero_results existing table", cursor);
        assertTrue("cursor mustn't contain any query result", cursor.getCount() == 0);
        destroyCursor(cursor);
        assertTrue("cursor must be destroyed", isCursorDestroyed(cursor));
    }

    @Test(expected = SQLiteException.class)
    public void testWhenQueryExecutedOnInvalidTableSQLiteExceptionOccurs() {
        Cursor cursor = null;
        try {
            final SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(INVALID_TABLE, DictionaryContract.Schema.ALL_COLUMNS, "", null, null, null, null, null);
        } catch (SQLiteException ex) {
            throw new SQLiteException();
        } finally {
            destroyCursor(cursor);
            assertTrue("cursor must be destroyed", isCursorDestroyed(cursor));
        }
    }

    @Test(expected = SQLiteException.class)
    public void testWhenQueryExecutedUsingInvalidColumnSQLiteExceptionOccurs() {
        Cursor cursor = null;
        try {
            final SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(DictionaryContract.Schema.TABLE_NAME, INVALID_COLUMN, "", null, null, null, null, null);
        } catch (SQLiteException ex) {
            throw new SQLiteException();
        } finally {
            destroyCursor(cursor);
            assertTrue("cursor must be destroyed", isCursorDestroyed(cursor));
        }
    }

    @Test
    public void testIfValidVocableInsertedInValidTableTheInsertionSucceed() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DictionaryContract.Schema.COLUMN_NAME, VALID_VOCABLE_NAME);

        final SQLiteDatabase db = getWritableDatabase();
        db.insert(DictionaryContract.Schema.TABLE_NAME, "", contentValues);

        Cursor cursor =
                db.query(DictionaryContract.Schema.TABLE_NAME, DictionaryContract.Schema.ALL_COLUMNS, "", null, null, null, null, null);

        assertTrue("query must return just one value", cursor.getCount() == 1);
        assertTrue("move to the first retrieved vocable position", cursor.moveToNext());
        assertEquals("the first valid_id must be equal to one", 1, cursor.getInt(0));
        assertEquals("the inserted vocable name must match with the expected value", VALID_VOCABLE_NAME, cursor.getString(1));

        destroyCursor(cursor);
        assertTrue("cursor must be destroyed", isCursorDestroyed(cursor));
    }

    private SQLiteDatabase getReadableDatabase() {
        return dbManager.getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return dbManager.getWritableDatabase();
    }

    private void destroyCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    private boolean isCursorDestroyed(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

}
