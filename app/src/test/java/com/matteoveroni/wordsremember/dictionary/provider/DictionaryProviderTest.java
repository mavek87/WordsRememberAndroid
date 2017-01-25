package com.matteoveroni.wordsremember.dictionary.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
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

    private final String VALID_ID_COLUMN_NAME = DictionaryContract.Schema.COLUMN_ID;
    private final long VALID_ID = 1;
    private final String ID_WITH_INVALID_TYPE = "IdWithInvalidType";

    private final String VALID_NAME_COLUMN_NAME = DictionaryContract.Schema.COLUMN_NAME;
    private final String VALID_NAME = "Name";

    private final String INVALID_COLUMN_NAME = "Invalid";

    @Before
    public void onStart() {
        values = new ContentValues();
        provider = Robolectric.setupContentProvider(DictionaryProvider.class);
        assertThat("no values should be inserted at the beginning ", values.keySet().isEmpty());
        assertNotNull("dictionary provider is not null ", provider);
    }

    @After
    public void tearDown() {
        // Reset DatabaseManager Singleton using reflections
        Util.resetSingleton(DatabaseManager.class, "DB_INSTANCE");
        values.clear();
    }

    @Test(expected = SQLiteException.class)
    public void insert_empty_content_value_throws_sqliteException() {
        assertThat("no values should be inserted at the beginning ", values.keySet().isEmpty());
        provider.insert(DictionaryContract.CONTENT_URI, values);
    }

    @Test(expected = SQLiteException.class)
    public void insert_content_value_with_wrong_sql_column_throws_sqliteException() {
        values.put(INVALID_COLUMN_NAME, VALID_ID);
        provider.insert(DictionaryContract.CONTENT_URI, values);
    }

    @Test(expected = SQLiteException.class)
    public void insert_content_value_with_wrong_type_for_column_throws_sqliteException() {
        values.put(VALID_ID_COLUMN_NAME, ID_WITH_INVALID_TYPE);
        provider.insert(DictionaryContract.CONTENT_URI, values);
    }

    @Test
    public void insert_real_vocable_works() {
        values.put(VALID_ID_COLUMN_NAME, VALID_ID);
        values.put(VALID_NAME_COLUMN_NAME, VALID_NAME);

        Uri generatedUri = provider.insert(DictionaryContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(DictionaryContract.CONTENT_URI + "/" + VALID_ID), generatedUri);
    }

}
