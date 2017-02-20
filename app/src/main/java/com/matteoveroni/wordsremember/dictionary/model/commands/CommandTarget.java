package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.net.Uri;

/**
 * Created by Matteo Veroni
 */

public abstract class CommandTarget {
    public enum Type {
        vocable, translation, vocableTranslation;
    }

    abstract Uri getContentUri();

    abstract void dispatchCompletionEvent(Uri uri);
}
