package com.matteoveroni.wordsremember.dictionary.model.commands;

import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;

/**
 * Created by Matteo Veroni
 */

class CommandTargetTranslation extends CommandTarget {

    CommandTargetTranslation() {
        super.contentUri = TranslationsContract.CONTENT_URI;
    }
}
