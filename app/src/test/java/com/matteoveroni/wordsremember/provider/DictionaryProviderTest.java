package com.matteoveroni.wordsremember.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.myutils.Singleton;
import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

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
    private Cursor cursor;
    private ContentValues values = new ContentValues();

    private final long VOCABLES_VALID_VOCABLE_ID = 1;
    private final String VOCABLES_VALID_VOCABLE = "Name";

    private final String TRANSLATIONS_VALID_TRANSLATION = "Name";

    @Before
    public void onStart() {
        provider = Robolectric.setupContentProvider(DictionaryProvider.class);
        assertNotNull("dictionary provider is not null ", provider);
        assertThat("no values should be inserted at the beginning ", values.keySet().isEmpty());
        assertThat("cursor should be null or closed", (cursor == null || cursor.isClosed()));
    }

    @After
    public void tearDown() {
        // Reset DatabaseManager Singleton using reflections (second parameter must match DatabaseManager instance name)
        Singleton.resetAttribute(DatabaseManager.class, "DB_UNIQUE_INSTANCE");
        values.clear();
        if (cursor != null) {
            cursor.close();
        }
    }

    /**********************************************************************************************/

    // Insertion tests

    /**********************************************************************************************/

    @Test
    public void insert_vocable_in_vocables_table_returns_expected_uri() {
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, VOCABLES_VALID_VOCABLE);
        Uri generatedUri = provider.insert(VocablesContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(VocablesContract.CONTENT_URI + "/" + 1), generatedUri);
    }

    @Test
    public void insert_translation_in_translations_table_returns_expected_uri() {
        values.put(TranslationsContract.Schema.COLUMN_TRANSLATION, TRANSLATIONS_VALID_TRANSLATION);
        Uri generatedUri = provider.insert(TranslationsContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(TranslationsContract.CONTENT_URI + "/" + 1), generatedUri);
    }

    @Test
    public void insert_record_in_vocablestranslations_table_returns_expected_uri() {
        values.put(TranslationsContract.Schema.COLUMN_TRANSLATION, TRANSLATIONS_VALID_TRANSLATION);
        Uri generatedUri = provider.insert(TranslationsContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(TranslationsContract.CONTENT_URI + "/" + 1), generatedUri);
    }

    /**********************************************************************************************/

    // Query tests

    /**********************************************************************************************/

    @Test
    public void select_vocable_by_id_on_empty_db_return_zero_results() {
        cursor = provider.query(
                Uri.parse(VocablesContract.CONTENT_URI + "/" + VOCABLES_VALID_VOCABLE_ID),
                VocablesContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        assertEquals("query should return no results", 0, cursor.getCount());
    }

    @Test
    public void select_vocable_by_id_on_db_containing_element_with_this_id_retrieve_the_right_vocable() {
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, VOCABLES_VALID_VOCABLE);
        Uri uri = provider.insert(VocablesContract.CONTENT_URI, values);
        assertEquals(
                "uri of inserted vocable is like expected",
                uri.toString(), VocablesContract.CONTENT_URI + "/" + VOCABLES_VALID_VOCABLE_ID
        );

        cursor = provider.query(
                uri,
                VocablesContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        assertEquals("query should return one result", 1, cursor.getCount());
        assertEquals("query should return the right id", cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_ID)), 1);
        assertEquals("query should return the right vocable", cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE)), VOCABLES_VALID_VOCABLE);
    }
}
