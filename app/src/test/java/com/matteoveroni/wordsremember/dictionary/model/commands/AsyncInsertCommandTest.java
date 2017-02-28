package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableComplete;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
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
    private Word vocable = new Word("testVocable");

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        app = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = app.getApplicationContext().getContentResolver();
    }

    @After
    public void tearDown() {
        eventBus.removeAllStickyEvents();
    }

    @Test
    public void test_NoStickyEvent_In_EventuBus_If_AnyOperationIsComplete() {
        assertNull("EventAsyncSaveVocableCompleted should NOT be fired before onInsertComplete",
                eventBus.getStickyEvent(EventAsyncSaveVocableCompleted.class)
        );
        assertNull(
                "EventAsyncUpdateVocableCompleted should NOT be fired before onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncUpdateVocableCompleted.class)
        );
        assertNull(
                "EventAsyncDeleteVocableCompleted should NOT be fired before onDeleteComplete",
                eventBus.getStickyEvent(EventAsyncDeleteVocableComplete.class)
        );
    }

    @Test
    public void test_asyncInsertVocableCommand_Fires_EventAsyncSaveVocableCompleted_onInsertComplete() {
        AsyncInsertCommand asyncInsertCommand = new AsyncInsertCommand(
                contentResolver, VocablesContract.CONTENT_URI, new ContentValues()
        );

        asyncInsertCommand.onInsertComplete(0, null, Uri.parse(VocablesContract.CONTENT_URI + "/1"));

        assertNotNull(
                "EventAsyncSaveVocableCompleted should be fired after onInsertComplete",
                eventBus.getStickyEvent(EventAsyncSaveVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncUpdateVocableCommand_Fires_EventAsyncUpdateVocableCompleted_onUpdateComplete_() {
        AsyncUpdateCommand asyncUpdateCommand = new AsyncUpdateCommand(
                contentResolver, VocablesContract.CONTENT_URI, new ContentValues(), "", new String[]{""}
        );

        asyncUpdateCommand.onUpdateComplete(0, null, 1);

        assertNotNull(
                "EventAsyncUpdateVocableCompleted should be fired after onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncUpdateVocableCompleted.class)
        );
    }

    @Test
    public void test_asyncDeleteVocableCommand_Fires_EventAsyncDeleteVocableCompleted_onDeleteComplete_() {
        AsyncDeleteCommand asyncDeleteCommand = new AsyncDeleteCommand(
                contentResolver, VocablesContract.CONTENT_URI, "", new String[]{""}
        );

        asyncDeleteCommand.onDeleteComplete(0, null, 1);

        assertNotNull(
                "EventAsyncUpdateVocableCompleted should be fired after onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncDeleteVocableComplete.class)
        );
    }
}
