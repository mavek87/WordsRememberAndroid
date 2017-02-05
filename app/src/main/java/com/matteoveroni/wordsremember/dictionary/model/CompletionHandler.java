package com.matteoveroni.wordsremember.dictionary.model;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * @author Matteo Veroni
 */

public abstract class CompletionHandler extends AsyncQueryHandler{

    public enum Type {
        vocable, translation, vocableTranslation;
    }

    CompletionHandler(ContentResolver contentResolver) {
        super(contentResolver);
    }

    abstract void execute();
}