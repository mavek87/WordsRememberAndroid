package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AsyncInsertCommandTest {

    private ShadowApplication app;
    private ContentResolver contentResolver;

    private EventBus eventBus = EventBus.getDefault();

    private static final ContentValues VALUES = new ContentValues();
    private static final Uri VOCABLES_URI = VocablesContract.CONTENT_URI;
    private static final Uri TRANSLATIONS_URI = TranslationsContract.CONTENT_URI;

    @Before
    public void setUp() {
        app = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = app.getApplicationContext().getContentResolver();
    }

    @After
    public void tearDown() {
        VALUES.clear();
        eventBus.removeAllStickyEvents();
    }

    @Test
    public void test_NoStickyEvent_In_EventuBus_If_AnyOperationIsComplete() {
        assertNull("EventAsyncSaveVocableCompleted should NOT be fired before onInsertComplete",
                eventBus.getStickyEvent(EventAsyncSaveVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncInsertVocableCommand_Fires_EventAsyncSaveVocableCompleted_onInsertComplete() {
        AsyncInsertCommand asyncInsertCommand = new AsyncInsertCommand(
                contentResolver, VOCABLES_URI, VALUES
        );

        asyncInsertCommand.onInsertComplete(0, null, Uri.parse(VOCABLES_URI + "/1"));

        assertNotNull(
                "EventAsyncSaveVocableCompleted should be fired after onInsertComplete",
                eventBus.getStickyEvent(EventAsyncSaveVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncInsertTranslationCommand_Fires_EventAsyncSaveTranslationCompleted_onInsertComplete() {
        AsyncInsertCommand asyncInsertCommand = new AsyncInsertCommand(
                contentResolver, TRANSLATIONS_URI, VALUES
        );

        asyncInsertCommand.onInsertComplete(0, null, Uri.parse(TRANSLATIONS_URI + "/1"));

        assertNotNull(
                "EventAsyncSaveTranslationCompleted should be fired after onInsertComplete",
                eventBus.getStickyEvent(EventAsyncSaveTranslationCompleted.class)
        );
    }
}
