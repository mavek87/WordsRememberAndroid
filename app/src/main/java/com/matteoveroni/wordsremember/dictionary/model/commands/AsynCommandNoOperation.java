package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;

/**
 * @author Matteo Veroni
 */

public class AsynCommandNoOperation extends AsyncCommand {

    public AsynCommandNoOperation(ContentResolver contentResolver) {
        super(contentResolver);
    }

    @Override
    public void execute() {
    }
}
