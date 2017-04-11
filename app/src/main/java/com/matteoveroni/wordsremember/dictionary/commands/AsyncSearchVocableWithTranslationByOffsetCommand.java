package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
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
                new String[]{"DISTINCT " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID},
                null,
                null,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_ID + " LIMIT 1 OFFSET " + offsetValue,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();

        String str_id = queryCompleteCursor.getString(queryCompleteCursor.getColumnIndex(VocablesTranslationsContract.Schema.COL_VOCABLE_ID));
        Log.i(TagGenerator.tag(AsyncSearchVocableWithTranslationByOffsetCommand.class), str_id);
        long idOfVocableWithTranslationByOffset = Long.valueOf(str_id);

        queryCompleteCursor.close();

        EventAsyncSearchVocableWithTranslationByOffsetCompleted event = new EventAsyncSearchVocableWithTranslationByOffsetCompleted(idOfVocableWithTranslationByOffset);
        EventBus.getDefault().postSticky(event);
    }
}
