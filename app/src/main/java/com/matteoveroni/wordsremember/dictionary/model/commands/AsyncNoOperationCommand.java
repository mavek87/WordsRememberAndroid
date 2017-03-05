package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;

/**
 * @author Matteo Veroni
 */

public class AsyncNoOperationCommand extends AsyncCommand {

    public AsyncNoOperationCommand(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public void execute() {
    }

    @Override
    void dispatchCompletionEvent() {
    }

}
