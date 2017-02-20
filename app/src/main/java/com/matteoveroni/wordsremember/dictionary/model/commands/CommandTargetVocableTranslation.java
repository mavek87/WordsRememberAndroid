package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.net.Uri;

import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

/**
 * Created by Matteo Veroni
 */

class CommandTargetVocableTranslation extends CommandTarget {

    @Override
    Uri getContentUri() {
        return VocablesTranslationsContract.CONTENT_URI;
    }

    @Override
    void dispatchCompletionEvent(Uri uri) {
    }
}
