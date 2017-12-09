package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSearchTranslationByNameCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Matteo Veroni
 */

public class AsyncSearchTranslationsByNameCommand extends AsyncQuerySearchCommand {

    public AsyncSearchTranslationsByNameCommand(ContentResolver contentResolver, String translationName, String orderBy) {
        this(contentResolver, translationName, orderBy, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncSearchTranslationsByNameCommand(ContentResolver contentResolver, String translationName, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                TranslationsContract.CONTENT_URI,
                TranslationsContract.Schema.ALL_COLUMNS,
                TranslationsContract.Schema.COL_TRANSLATION + "=?",
                new String[]{translationName},
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        List<Word> maxOneTranslationWithSameNameList = DictionaryDAO.cursorToListOfTranslations(queryCompleteCursor);

        EventAsyncSearchTranslationByNameCompleted event = new EventAsyncSearchTranslationByNameCompleted(
                maxOneTranslationWithSameNameList.size() == 1
                        ? maxOneTranslationWithSameNameList.get(0)
                        : null
        );
        EventBus.getDefault().postSticky(event);

        queryCompleteCursor.close();
    }
}
