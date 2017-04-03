package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;
import android.database.DatabaseUtils;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventCountUniqueVocablesWithTranslationsCompleted;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
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
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                new String[]{"DISTINCT " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID},
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
        EventBus.getDefault().postSticky(new EventCountUniqueVocablesWithTranslationsCompleted(numberOfCountResults));
        queryCompleteCursor.close();
    }
}
