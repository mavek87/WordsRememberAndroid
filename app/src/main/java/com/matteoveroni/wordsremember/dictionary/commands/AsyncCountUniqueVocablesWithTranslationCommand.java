package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountUniqueVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncCountUniqueVocablesWithTranslationCommand extends AsyncQueryCommand {

    public AsyncCountUniqueVocablesWithTranslationCommand(ContentResolver contentResolver) {
        this(contentResolver, null, null, null, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncCountUniqueVocablesWithTranslationCommand(ContentResolver contentResolver, String selection, String[] selectionArgs, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesTranslationsContract.CONTENT_URI,
                new String[]{"COUNT (*) FROM (SELECT DISTINCT " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + " FROM " + VocablesTranslationsContract.Schema.TABLE_NAME + ")"},
                selection,
                selectionArgs,
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();
        int numberOfCountResults = queryCompleteCursor.getInt(0);
        EventBus.getDefault().postSticky(new EventCountUniqueVocablesWithTranslationsCompleted(numberOfCountResults));
    }
}
