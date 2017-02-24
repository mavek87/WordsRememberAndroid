package com.matteoveroni.wordsremember.dictionary.model.commands;

import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

/**
 * Created by Matteo Veroni
 */

class CommandTargetVocableTranslation extends CommandTarget {

    CommandTargetVocableTranslation() {
        super.contentUri = VocablesTranslationsContract.CONTENT_URI;
    }
}
