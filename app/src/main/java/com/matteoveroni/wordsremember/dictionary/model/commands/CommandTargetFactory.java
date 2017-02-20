package com.matteoveroni.wordsremember.dictionary.model.commands;

/**
 * Created by Matteo Veroni
 */

public class CommandTargetFactory {

    private CommandTargetFactory() {
    }

    public static CommandTarget create(CommandTarget.Type type) {
        switch (type) {
            case vocable:
                return new CommandTargetVocable();
            case translation:
                return new CommandTargetTranslation();
            case vocableTranslation:
                return new CommandTargetVocableTranslation();
            default:
                throw new RuntimeException("Unknown command type passed to commandFactory");
        }
    }
}
