package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncFindVocablesByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchVocablesByNameCommand extends AsyncQueryCommand {

    public AsyncSearchVocablesByNameCommand(ContentResolver contentResolver, String vocableName, String orderBy) {
        this(contentResolver, vocableName, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchVocablesByNameCommand(ContentResolver contentResolver, String vocableName, String orderBy, Object nextCommand) {
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
        EventAsyncFindVocablesByNameCompleted event =
                new EventAsyncFindVocablesByNameCompleted(DictionaryDAO.cursorToListOfVocables(queryCompleteCursor));
        EventBus.getDefault().postSticky(event);
    }
}
