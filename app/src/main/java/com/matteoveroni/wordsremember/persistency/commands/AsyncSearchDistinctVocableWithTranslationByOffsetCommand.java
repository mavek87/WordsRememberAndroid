package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchDistinctVocableWithTranslationByOffsetCommand extends AsyncQuerySearchCommand {

    public AsyncSearchDistinctVocableWithTranslationByOffsetCommand(ContentResolver contentResolver, String offsetValue) {
        this(contentResolver, offsetValue, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchDistinctVocableWithTranslationByOffsetCommand(ContentResolver contentResolver, String offsetValue, Object nextCommand) {
        super(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                new String[]{"DISTINCT " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID},
                null,
                null,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_ID + " LIMIT 1 OFFSET " + offsetValue,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();

        String str_id = queryCompleteCursor.getString(queryCompleteCursor.getColumnIndex(VocablesTranslationsContract.Schema.COL_VOCABLE_ID));
        Log.i(TagGenerator.tag(AsyncSearchDistinctVocableWithTranslationByOffsetCommand.class), str_id);
        long idOfVocableWithTranslationByOffset = Long.valueOf(str_id);

        EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted event = new EventAsyncSearchDistinctVocableWithTranslationByOffsetCompleted(idOfVocableWithTranslationByOffset);
        EventBus.getDefault().post(event);

        queryCompleteCursor.close();
    }
}
