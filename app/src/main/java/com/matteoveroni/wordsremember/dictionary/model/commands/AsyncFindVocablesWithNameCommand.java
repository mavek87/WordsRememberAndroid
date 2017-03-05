package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncFindVocablesWithSearchedNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;

/**
 * Created by Matteo Veroni
 */

public class AsyncFindVocablesWithNameCommand extends AsyncQueryCommand {

    public AsyncFindVocablesWithNameCommand(ContentResolver contentResolver, String vocableName, String orderBy) {
        this(contentResolver, vocableName, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncFindVocablesWithNameCommand(ContentResolver contentResolver, String vocableName, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesContract.CONTENT_URI,
                VocablesContract.Schema.ALL_COLUMNS,
                VocablesContract.Schema.COLUMN_VOCABLE + "=?",
                new String[]{vocableName},
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        EventAsyncFindVocablesWithSearchedNameCompleted event =
                new EventAsyncFindVocablesWithSearchedNameCompleted(DictionaryDAO.cursorToListOfVocables(queryCompleteCursor));
        EventBus.getDefault().postSticky(event);
    }
}
