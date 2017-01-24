package com.matteoveroni.wordsremember.dictionary.provider;

import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DictionaryProviderTest {

    private final List<DictionaryProvider> providersForTests = new ArrayList<>();

//    private DictionaryProvider provider;

    public synchronized int createNewProvider() {
        int i = providersForTests.size();
        providersForTests.add(Robolectric.setupContentProvider(DictionaryProvider.class));
        return i;
    }

    private ContentValues values;

    private final long VALID_ID = 1;

    private final String VALID_NAME = "Name";
    private final int INVALID_NAME = 12345;

//    @Before
//    public void onStart() {
//        provider = createNewProvider();
//    }

//    @After
//    public void tearDown() {
//        ActiveAndroid.dispose();
////        provider.shutdown();
//    }

    @Test(expected = Exception.class)
    public void testInsertEmptyVocableThrowsException() {
        ContentValues values = new ContentValues();
        DictionaryProvider provider = providersForTests.get(createNewProvider());
        provider.insert(DictionaryContract.CONTENT_URI, values);
    }

    @Test
    public void testInsertRealVocableWorks() {
        ContentValues values = new ContentValues();
        values.put(DictionaryContract.Schema.COLUMN_ID, VALID_ID);
        values.put(DictionaryContract.Schema.COLUMN_NAME, VALID_NAME);

        DictionaryProvider provider = providersForTests.get(createNewProvider());
        final Uri generatedUri = provider.insert(DictionaryContract.CONTENT_URI, values);
        assertEquals("generated uri is like expected", Uri.parse(DictionaryContract.CONTENT_URI + "/" + VALID_ID), generatedUri);
    }
//
//    @Test(expected = Exception.class)
//    public void testInsertInvalidVocableThrowsException() {
//        ContentValues values = new ContentValues();
//        values.put(DictionaryContract.Schema.COLUMN_ID, VALID_ID);
//        values.put(DictionaryContract.Schema.COLUMN_NAME, INVALID_NAME);
//        provider.insert(DictionaryContract.CONTENT_URI, values);
//    }
}
