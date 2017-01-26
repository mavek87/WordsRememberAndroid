package com.matteoveroni.wordsremember.dictionary.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.utilities.Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DictionaryProviderTest {

    private DictionaryProvider provider;
    private ContentValues values;
    private Cursor cursor;

    private final String VALID_ID_COLUMN_NAME = VocablesContract.Schema.COLUMN_ID;
    private final long VALID_ID = 1;
    private final String ID_WITH_INVALID_TYPE = "IdWithInvalidType";

    private final String VALID_NAME_COLUMN_NAME = VocablesContract.Schema.COLUMN_NAME;
    private final String VALID_NAME = "Name";

    private final String INVALID_COLUMN_NAME = "Invalid";

    @Before
    public void onStart() {
        values = new ContentValues();
        provider = Robolectric.setupContentProvider(DictionaryProvider.class);
        assertNotNull("dictionary provider is not null ", provider);
        assertThat("no values should be inserted at the beginning ", values.keySet().isEmpty());
        assertThat("cursor should be null or closed", (cursor == null || cursor.isClosed()));
    }

    @After
    public void tearDown() {
        // Reset DatabaseManager Singleton using reflections
        Util.resetSingleton(DatabaseManager.class, "DB_INSTANCE");
        values.clear();
        if (cursor != null) {
            cursor.close();
        }
    }

    /**********************************************************************************************/

    // Insertion tests

    /**********************************************************************************************/

    @Test(expected = SQLiteException.class)
    public void insert_empty_content_value_throws_sqliteException() {
        assertThat("no values should be inserted at the beginning ", values.keySet().isEmpty());
        provider.insert(VocablesContract.CONTENT_URI, values);
    }

    @Test(expected = SQLiteException.class)
    public void insert_vocable_with_wrong_sql_column_throws_sqliteException() {
        values.put(INVALID_COLUMN_NAME, VALID_ID);
        provider.insert(VocablesContract.CONTENT_URI, values);
    }

    @Test(expected = SQLiteException.class)
    public void insert_vocable_with_wrong_type_for_column_throws_sqliteException() {
        values.put(VALID_ID_COLUMN_NAME, ID_WITH_INVALID_TYPE);
        provider.insert(VocablesContract.CONTENT_URI, values);
    }

    @Test
    public void insert_real_vocable_works() {
        Uri generatedUri = insertValidVocable();
        assertEquals("generated uri is like expected", Uri.parse(VocablesContract.CONTENT_URI + "/" + VALID_ID), generatedUri);
    }

    /**********************************************************************************************/

    // Query tests

    /**********************************************************************************************/

    @Test
    public void select_query_by_id_on_empty_db_return_zero_results() {
        cursor = provider.query(
                Uri.parse(VocablesContract.CONTENT_URI + "/" + VALID_ID),
                VocablesContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        assertEquals("query should return no results", 0, cursor.getCount());
    }

    @Test
    public void select_query_by_id_on_db_containing_element_with_this_id_retrieve_the_right_result() {
        insertValidVocable();
        cursor = provider.query(
                Uri.parse(VocablesContract.CONTENT_URI + "/" + VALID_ID),
                VocablesContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        assertEquals("query should return no results", 1, cursor.getCount());
        assertEquals("query should return the right id", cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_ID)), VALID_ID);
        assertEquals("query should return the right id", cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_NAME)), VALID_NAME);
    }

    /**********************************************************************************************/

    // Helper methods

    /**********************************************************************************************/

    private Uri insertValidVocable() {
        values.put(VALID_ID_COLUMN_NAME, VALID_ID);
        values.put(VALID_NAME_COLUMN_NAME, VALID_NAME);
        return provider.insert(VocablesContract.CONTENT_URI, values);
    }

}
