package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;

/**
 * @author Matteo Veroni
 */

abstract class CompletionHandler extends AsyncQueryHandler {

    public CompletionHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    abstract public void execute();
}