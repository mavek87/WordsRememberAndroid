package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.UriMatcher;

/**
 * @author Matteo Veroni
 */

public abstract class AsyncCommand extends AsyncQueryHandler {

    AsyncCommand(ContentResolver contentResolver) {
        super(contentResolver);
    }

    abstract void execute();
}