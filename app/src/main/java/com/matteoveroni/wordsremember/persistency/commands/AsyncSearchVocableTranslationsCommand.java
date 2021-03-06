package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchVocableTranslationsCommand extends AsyncQuerySearchCommand {

    private final Word vocable;

    public AsyncSearchVocableTranslationsCommand(ContentResolver contentResolver, Word vocable, String orderBy) {
        this(contentResolver, vocable, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchVocableTranslationsCommand(ContentResolver contentResolver, Word vocable, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_URI,
                null,
                null,
                new String[]{String.valueOf(vocable.getId())},
                orderBy,
                nextCommand
        );
        this.vocable = vocable;
    }

    @Override
    public void dispatchCompletionEvent() {
        List<Word> foundTranslations = DictionaryDAO.cursorToListOfTranslations(queryCompleteCursor);

        EventAsyncSearchVocableTranslationsCompleted event = new EventAsyncSearchVocableTranslationsCompleted(
                vocable,
                foundTranslations
        );
        EventBus.getDefault().postSticky(event);

        queryCompleteCursor.close();
    }
}
