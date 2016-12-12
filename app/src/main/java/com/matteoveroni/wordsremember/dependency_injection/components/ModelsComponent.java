package com.matteoveroni.wordsremember.dependency_injection.components;

import com.matteoveroni.wordsremember.dependency_injection.modules.AppModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.ModelsModule;
import com.matteoveroni.wordsremember.dependency_injection.modules.PresentersModule;
import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementActivity;
import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementActivityPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;

import dagger.Component;

//@Component(modules = {AppModule.class, ModelsModule.class})
public interface ModelsComponent {
//    void inject(DictionaryManagementActivityPresenter dictionaryModel);
}
