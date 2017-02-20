package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.net.Uri;

import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;

/**
 * Created by Matteo Veroni
 */

class CommandTargetTranslation extends CommandTarget {

    @Override
    Uri getContentUri() {
        return TranslationsContract.CONTENT_URI;
    }

    @Override
    public void dispatchCompletionEvent(Uri uriOfInsertedRow) {
    }

}
