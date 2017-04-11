package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchVocablesByNameCommand extends AsyncQuerySearchCommand {

    public AsyncSearchVocablesByNameCommand(ContentResolver contentResolver, String vocableName, String orderBy) {
        this(contentResolver, vocableName, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchVocablesByNameCommand(ContentResolver contentResolver, String vocableName, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesContract.CONTENT_URI,
                VocablesContract.Schema.ALL_COLUMNS,
                VocablesContract.Schema.COL_VOCABLE + "=?",
                new String[]{vocableName},
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        // Todo: remove list
        List<Word> maxOneVocableWithSameNameList = DictionaryDAO.cursorToListOfVocables(queryCompleteCursor);
        queryCompleteCursor.close();

        EventAsyncSearchVocableCompleted event = new EventAsyncSearchVocableCompleted
                (
                        maxOneVocableWithSameNameList.size() == 1
                                ? maxOneVocableWithSameNameList.get(0)
                                : null
                );
        EventBus.getDefault().postSticky(event);
    }
}
