package com.matteoveroni.wordsremember.dictionary.model.commands;

import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

/**
 * Created by Matteo Veroni
 */

class CommandTargetVocable extends CommandTarget {

    CommandTargetVocable() {
        super.contentUri = VocablesContract.CONTENT_URI;
    }

}
