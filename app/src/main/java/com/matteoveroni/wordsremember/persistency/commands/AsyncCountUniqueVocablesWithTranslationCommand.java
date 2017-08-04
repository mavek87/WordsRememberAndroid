package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountDistinctVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncCountUniqueVocablesWithTranslationCommand extends AsyncQuerySearchCommand {

    public AsyncCountUniqueVocablesWithTranslationCommand(ContentResolver contentResolver) {
        this(contentResolver, null, null, null, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncCountUniqueVocablesWithTranslationCommand(ContentResolver contentResolver, String selection, String[] selectionArgs, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                new String[]{"DISTINCT " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID},
                selection,
                selectionArgs,
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();
        int numberOfCountResults = queryCompleteCursor.getCount();
        EventBus.getDefault().postSticky(new EventCountDistinctVocablesWithTranslationsCompleted(numberOfCountResults));
        queryCompleteCursor.close();
    }
}
