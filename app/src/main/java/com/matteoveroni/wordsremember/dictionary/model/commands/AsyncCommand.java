package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * @author Matteo Veroni
 */

public abstract class AsyncCommand extends AsyncQueryHandler{

    AsyncCommand(ContentResolver contentResolver) {
        super(contentResolver);
    }

    abstract void execute();
}