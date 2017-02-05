package com.matteoveroni.wordsremember.dictionary.model;

import android.content.ContentResolver;

/**
 * @author Matteo Veroni
 */

public class NoOperationCommand extends CompletionHandler {

    public NoOperationCommand(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public void execute() {
    }
}
