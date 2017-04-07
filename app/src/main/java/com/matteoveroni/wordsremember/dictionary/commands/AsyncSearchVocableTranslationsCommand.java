package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchVocableTranslationsCommand extends AsyncQueryCommand {

    public AsyncSearchVocableTranslationsCommand(ContentResolver contentResolver, String vocableId, String orderBy) {
        this(contentResolver, vocableId, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchVocableTranslationsCommand(ContentResolver contentResolver, String vocableId, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_URI,
                TranslationsContract.Schema.ALL_COLUMNS,
                TranslationsContract.Schema.TABLE_DOT_COL_ID + "=?",
                new String[]{vocableId},
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        List<Word> foundTranslations = DictionaryDAO.cursorToListOfTranslations(queryCompleteCursor);
        EventAsyncSearchVocableTranslationsCompleted event = new EventAsyncSearchVocableTranslationsCompleted(
                foundTranslations
        );
        EventBus.getDefault().postSticky(event);
    }
}
