package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSearchVocableWithTranslationByOffsetCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchVocableWithTranslationByOffsetCommand extends AsyncQueryCommand {

    public AsyncSearchVocableWithTranslationByOffsetCommand(ContentResolver contentResolver, String offsetValue) {
        this(contentResolver, offsetValue, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchVocableWithTranslationByOffsetCommand(ContentResolver contentResolver, String offsetValue, Object nextCommand) {
        super(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                VocablesTranslationsContract.Schema.ALL_COLUMNS,
                null,
                null,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_ID + " LIMIT 1 OFFSET " + offsetValue,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();

        Word vocableWithTranslationByOffset = (queryCompleteCursor.isBeforeFirst())
                ? null
                : DictionaryDAO.cursorToVocable(queryCompleteCursor);

        EventAsyncSearchVocableWithTranslationByOffsetCompleted event = new EventAsyncSearchVocableWithTranslationByOffsetCompleted(vocableWithTranslationByOffset);
        EventBus.getDefault().postSticky(event);
    }
}
