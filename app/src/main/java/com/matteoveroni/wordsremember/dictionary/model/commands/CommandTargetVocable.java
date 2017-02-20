package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Matteo Veroni
 */

class CommandTargetVocable extends CommandTarget {

    @Override
    Uri getContentUri() {
        return VocablesContract.CONTENT_URI;
    }

    public void dispatchCompletionEvent(Uri uriOfInsertedRow) {
        EventAsyncSaveVocableCompleted event = null;
        String errorMessage = "Insertion failed";
        try {
            String idOfInsertedRow = uriOfInsertedRow.getLastPathSegment();
            if (!idOfInsertedRow.isEmpty())
                event = new EventAsyncSaveVocableCompleted(Long.valueOf(idOfInsertedRow));

        } catch (Exception ex) {
            errorMessage += "\n" + ex.getMessage();
        } finally {
            if (event == null) {
                event = new EventAsyncSaveVocableCompleted(-1);
                event.setErrorMessage(errorMessage);
            }
        }
        EventBus.getDefault().postSticky(event);
    }

}
