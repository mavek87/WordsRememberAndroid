package com.matteoveroni.wordsremember.persistency.commands;

import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.users.User;

/**
 * @author Matteo Veroni
 */

public class CommandStoreAndSetAppUser implements Command {

    private final User user;
    private final Settings settings;
    private final DBManager dbManager;

    public CommandStoreAndSetAppUser(User user, Settings settings, DBManager dbManager) {
        this.user = user;
        this.settings = settings;
        this.dbManager = dbManager;
    }

    @Override
    public void execute() {
        settings.saveUser(user);
        dbManager.setupUserDBHelper(user);
    }
}
