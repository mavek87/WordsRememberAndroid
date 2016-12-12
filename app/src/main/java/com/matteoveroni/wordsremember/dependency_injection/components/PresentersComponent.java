package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.PresentersModule;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;

import dagger.Component;

@Component(modules = {PresentersModule.class})
public interface PresentersComponent {
    void inject(MainMenuActivity activity);
}
