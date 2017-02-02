package com.matteoveroni.wordsremember.dictionary.model.async_commands;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * @author Matteo Veroni
 */

abstract class CompletionHandler extends AsyncQueryHandler{

    public CompletionHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    abstract void execute();
}