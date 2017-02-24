package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.net.Uri;

/**
 * Created by Matteo Veroni
 */

public abstract class CommandTarget {

    public enum Type {
        VOCABLE, TRANSLATION, VOCABLE_TRANSLATION;
    }

    Uri contentUri;

    Uri getContentUri() {
        return this.contentUri;
    }
}
