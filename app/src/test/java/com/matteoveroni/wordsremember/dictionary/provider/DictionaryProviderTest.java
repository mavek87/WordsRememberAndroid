package com.matteoveroni.wordsremember.dictionary.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
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

//    private final String VOCABLES_COLUMN_ID = VocablesContract.Schema.COLUMN_ID;
//    private final String VOCABLES_COLUMN_VOCABLE = VocablesContract.Schema.COLUMN_VOCABLE;

    private final long VOCABLES_VALID_VOCABLE_ID = 1;
    private final String VOCABLES_VALID_VOCABLE = "Name";

//    private final String TRANSLATIONS_COLUMN_ID = VocablesContract.Schema.COLUMN_ID;
//    private final String TRANSLATIONS_COLUMN_TRANSLATION = VocablesContract.Schema.COLUMN_VOCABLE;

    private final long TRANSLATIONS_VALID_TRANSLATION_ID = 1;
    private final String TRANSLATIONS_VALID_TRANSLATION = "Name";

    private final String ID_WITH_INVALID_TYPE = "IdWithInvalidType";
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

    @Test
    public void insert_vocable_in_vocables_table_returns_expected_uri() {
        values.put(VocablesContract.Schema.COLUMN_ID, VOCABLES_VALID_VOCABLE_ID);
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, VOCABLES_VALID_VOCABLE);
        Uri generatedUri = provider.insert(VocablesContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(VocablesContract.CONTENT_URI + "/" + VOCABLES_VALID_VOCABLE_ID), generatedUri);
    }

    @Test
    public void insert_translation_in_translations_table_returns_expected_uri() {
        values.put(TranslationsContract.Schema.COLUMN_ID, 1);
        values.put(TranslationsContract.Schema.COLUMN_TRANSLATION, TRANSLATIONS_VALID_TRANSLATION);
        Uri generatedUri = provider.insert(TranslationsContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(TranslationsContract.CONTENT_URI + "/" + 1), generatedUri);
    }

    @Test
    public void insert_record_in_vocablestranslations_table_returns_expected_uri() {
        values.put(VocablesTranslationsContract.Schema.COLUMN_ID, 1);
        values.put(TranslationsContract.Schema.COLUMN_TRANSLATION, TRANSLATIONS_VALID_TRANSLATION);
        Uri generatedUri = provider.insert(TranslationsContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(TranslationsContract.CONTENT_URI + "/" + 1), generatedUri);
    }


    /**********************************************************************************************/

    // Query tests

    /**********************************************************************************************/

    @Test
    public void select_query_by_id_on_empty_db_return_zero_results() {
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
    public void select_query_by_id_on_db_containing_element_with_this_id_retrieve_the_right_result() {
        values.put(VocablesContract.Schema.COLUMN_ID, VOCABLES_VALID_VOCABLE_ID);
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, VOCABLES_VALID_VOCABLE);
        provider.insert(VocablesContract.CONTENT_URI, values);
        cursor = provider.query(
                Uri.parse(VocablesContract.CONTENT_URI + "/" + VOCABLES_VALID_VOCABLE_ID),
                VocablesContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        assertEquals("query should return no results", 1, cursor.getCount());
        assertEquals("query should return the right id", cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_ID)), VOCABLES_VALID_VOCABLE_ID);
        assertEquals("query should return the right id", cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE)), VOCABLES_VALID_VOCABLE);
    }

    @Test
    public void selectVocableTranslations() {
        values.put(VocablesTranslationsContract.Schema.COLUMN_ID, "1");
        values.put(VocablesTranslationsContract.Schema.COLUMN_VOCABLE_ID, "2");
        values.put(VocablesTranslationsContract.Schema.COLUMN_TRANSLATION_ID, "1");
        assertNotNull(provider.insert(VocablesTranslationsContract.CONTENT_URI, values));

        values.clear();
        values.put(VocablesTranslationsContract.Schema.COLUMN_ID, "2");
        values.put(VocablesTranslationsContract.Schema.COLUMN_VOCABLE_ID, "2");
        values.put(VocablesTranslationsContract.Schema.COLUMN_TRANSLATION_ID, "2");
        assertNotNull(provider.insert(VocablesTranslationsContract.CONTENT_URI, values));

        cursor = provider.query(
                Uri.parse(VocablesTranslationsContract.CONTENT_URI + "/"),
                VocablesTranslationsContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        assertEquals("query should return two results", 2, cursor.getCount());
        cursor.moveToFirst();

        cursor = provider.query(
                Uri.parse(VocablesTranslationsContract.CONTENT_URI + "/"),
                VocablesTranslationsContract.Schema.ALL_COLUMNS,
                VocablesTranslationsContract.Schema.COLUMN_VOCABLE_ID + " = ?",
                new String[]{"2"},
                null
        );
        assertEquals("query should return one result", 2, cursor.getCount());
    }
}
