package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchVocableTranslationsCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

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
//        super(
//                contentResolver,
//                VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_URI,
//                TranslationsContract.Schema.ALL_COLUMNS,
//                TranslationsContract.Schema.TABLE_DOT_COL_ID + "=?",
//                new String[]{"" + vocable.getId()},
//                orderBy,
//                nextCommand
//        );
        // CURRENT WRONG QUERY

        //SELECT translations._id, translations.translation
        // FROM translations LEFT JOIN vocables_translations
        // ON (translations._id=vocables_translations.translation_id)
        // WHERE (translations._id=?)

        // CORRECT QUERY SHOULD BE

        super(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                TranslationsContract.Schema.ALL_COLUMNS,
                null,
                null,
                orderBy + " LEFT JOIN " + TranslationsContract.Schema.TABLE_NAME +
                        " ON (" + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + "=" + TranslationsContract.Schema.TABLE_DOT_COL_ID + ")" +
                        "WHERE (" + VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "= "+ "" + vocable.getId() +")",
                nextCommand
        );

        // SELECT translations._id, translations.translation
        // FROM vocables_translations LEFT JOIN translations
        // ON (translations._id=vocables_translations.translation_id)
        // WHERE (vocables_translations.vocable_id=13)
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
    }
}
