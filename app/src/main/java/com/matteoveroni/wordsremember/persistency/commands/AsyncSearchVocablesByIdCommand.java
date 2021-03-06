package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchVocablesByIdCommand extends AsyncQuerySearchCommand {

    public AsyncSearchVocablesByIdCommand(ContentResolver contentResolver, long vocableId, String orderBy) {
        this(contentResolver, vocableId, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchVocablesByIdCommand(ContentResolver contentResolver, long vocableId, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesContract.CONTENT_URI,
                VocablesContract.Schema.ALL_COLUMNS,
                VocablesContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{String.valueOf(vocableId)},
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();

        Word vocable = (queryCompleteCursor.isBeforeFirst())
                ? null
                : DictionaryDAO.cursorToVocable(queryCompleteCursor);

        EventBus.getDefault().postSticky(new EventAsyncSearchVocableCompleted(vocable));

        queryCompleteCursor.close();
    }
}
