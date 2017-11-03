package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;

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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
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

    private final EventBus EVENT_BUS = EventBus.getDefault();

    private static final Uri VOCABLES_URI = VocablesContract.CONTENT_URI;
    private static final Uri TRANSLATIONS_URI = TranslationsContract.CONTENT_URI;
    private static final ContentValues VALUES = new ContentValues();
    private static final long FAKE_INSERTED_ID = 1;

    @Before
    public void setUp() {
        app = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = app.getApplicationContext().getContentResolver();
    }

    @After
    public void tearDown() {
        VALUES.clear();
        EVENT_BUS.removeAllStickyEvents();
    }

    @Test
    public void test_NoStickyEvent_In_EventuBus_If_AnyOperationIsComplete() {
        assertNull("EventAsyncSaveVocableCompleted should NOT be fired before onInsertComplete",
                EVENT_BUS.getStickyEvent(EventAsyncSaveVocableCompleted.class)
        );
        assertNull("EventAsyncSaveTranslationCompleted should NOT be fired before onInsertComplete",
                EVENT_BUS.getStickyEvent(EventAsyncSaveTranslationCompleted.class)
        );
    }

    @Test
    public void test_asyncInsertVocableCommand_Fires_EventAsyncSaveVocableCompleted_onInsertComplete() {
        AsyncInsertCommand asyncInsertCommand = new AsyncInsertCommand(contentResolver, VOCABLES_URI, VALUES);

        asyncInsertCommand.onInsertComplete(0, null, Uri.parse(VOCABLES_URI + "/" + FAKE_INSERTED_ID));

        EventAsyncSaveVocableCompleted event = EVENT_BUS.getStickyEvent(EventAsyncSaveVocableCompleted.class);
        assertEquals(
                "EventAsyncSaveVocableCompleted should be fired onInsertComplete with right id",
                FAKE_INSERTED_ID, event.getSavedVocableId()
        );
    }

    @Test
    public void test_asyncInsertTranslationCommand_Fires_EventAsyncSaveTranslationCompleted_onInsertComplete() {
        AsyncInsertCommand asyncInsertCommand = new AsyncInsertCommand(contentResolver, TRANSLATIONS_URI, VALUES);

        asyncInsertCommand.onInsertComplete(0, null, Uri.parse(TRANSLATIONS_URI + "/" + FAKE_INSERTED_ID));

        EventAsyncSaveTranslationCompleted event = EVENT_BUS.getStickyEvent(EventAsyncSaveTranslationCompleted.class);
        assertEquals(
                "EventAsyncSaveTranslationCompleted should be fired onInsertComplete with right id",
                FAKE_INSERTED_ID, event.getSavedTranslationId()
        );
    }
}
