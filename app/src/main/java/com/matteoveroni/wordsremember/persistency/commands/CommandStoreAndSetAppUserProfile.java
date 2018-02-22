package com.matteoveroni.wordsremember.persistency.commands;

import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;

/**
 * @author Matteo Veroni
 */

public class CommandStoreAndSetAppUserProfile implements Command {

    private final Profile userProfile;
    private final Settings settings;
    private final DBManager dbManager;

    public CommandStoreAndSetAppUserProfile(Profile userProfile, Settings settings, DBManager dbManager) {
        this.userProfile = userProfile;
        this.settings = settings;
        this.dbManager = dbManager;
    }

    @Override
    public void execute() {
        settings.saveUserProfile(userProfile);
        dbManager.setupUserProfileDBHelper(userProfile);
    }
}
