package com.matteoveroni.wordsremember.dictionary.model.commands;

/**
 * Created by Matteo Veroni
 */

public class CommandTargetFactory {

    private CommandTargetFactory() {
    }

    public static CommandTarget create(CommandTarget.Type type) {
        switch (type) {
            case VOCABLE:
                return new CommandTargetVocable();
            case TRANSLATION:
                return new CommandTargetTranslation();
            case VOCABLE_TRANSLATION:
                return new CommandTargetVocableTranslation();
            default:
                throw new RuntimeException("Unknown command type passed to commandFactory");
        }
    }
}
