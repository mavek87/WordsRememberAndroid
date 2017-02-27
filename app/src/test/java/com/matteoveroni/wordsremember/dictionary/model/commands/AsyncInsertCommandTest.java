package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;

/**
 * Created by Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AsyncInsertCommandTest {

    private ShadowApplication application;
    private ContentResolver contentResolver;
    private EventBus eventBus;

    private Word vocable = new Word("testVocable");

    @Before
    public void setUp() {
        application = Shadows.shadowOf(RuntimeEnvironment.application);
        contentResolver = application.getApplicationContext().getContentResolver();
        eventBus = EventBus.getDefault();
    }

    @Test
    public void test_asyncInsertVocableCommand_onInsertComplete_Fires_AsyncSaveVocableCompleted() {
        ContentValues values = new ContentValues();
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, vocable.getName());
        AsyncInsertCommand asyncInsertCommand = new AsyncInsertCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                values
        );

        assertNull(eventBus.getStickyEvent(EventAsyncSaveVocableCompleted.class));

        asyncInsertCommand.onInsertComplete(0, null, Uri.parse(VocablesContract.CONTENT_URI + "/1"));

        assertNotNull(eventBus.getStickyEvent(EventAsyncSaveVocableCompleted.class));
    }

    @Test
    public void test_asyncUpdateVocableCommand_onUpdateComplete_Fires_AsyncUpdateVocableCompleted() {
        ContentValues values = new ContentValues();
        values.put(VocablesContract.Schema.COLUMN_VOCABLE, "updatedVocableName");

        AsyncUpdateCommand asyncUpdateCommand = new AsyncUpdateCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                values,
                VocablesContract.Schema.COLUMN_ID + " = ?",
                new String[]{Long.toString(vocable.getId())}
        );

        assertNull(
                "EventAsyncUpdateVocableCompleted should NOT be fired before onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncUpdateVocableCompleted.class)
        );

        asyncUpdateCommand.onUpdateComplete(0, null, 1);

        assertNotNull(
                "EventAsyncUpdateVocableCompleted should be fired after onUpdateComplete",
                eventBus.getStickyEvent(EventAsyncUpdateVocableCompleted.class)
        );
    }
}
