package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementViewLayoutManager;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    public ModelModule() {
    }

    @Provides
    @SuppressWarnings("unused")
    public DictionaryDAO provideDictionaryManagementPresenterModel(Context context) {
        return new DictionaryDAO(context);
    }

    @Provides
    @SuppressWarnings("unused")
    public DictionaryManagementViewLayoutManager provideDictionaryManagementViewLayoutManager() {
        return new DictionaryManagementViewLayoutManager();
    }

}
