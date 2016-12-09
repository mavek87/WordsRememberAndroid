package com.matteoveroni.wordsremember.dictionary.management.activity;

import android.view.View;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.management.activity.interfaces.DictionaryManagementActivityPresenter;
import com.matteoveroni.wordsremember.dictionary.management.activity.layout.DictionaryManagementActivityLayoutManager;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.models.NullObjectProxy;
import com.matteoveroni.wordsremember.models.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementActivityConcretePresenter implements DictionaryManagementActivityPresenter {

    private View view;
    private DictionaryDAO model;
    private DictionaryManagementActivityLayoutManager layoutManager;

    @Override
    public void onViewAttached(Object view) {
        this.view = (View) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{View.class},
                new NullObjectProxy(view));

        if (model == null && this.view.getContext() != null) {
            model = new DictionaryDAO(this.view.getContext());
        }
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void onDestroyed() {
        onViewDetached();
    }

    private void populateDatabase() {
        Word firstVocableToSave = new Word("test123");
        model.saveVocable(firstVocableToSave);

        Word secondVocableToSave = new Word("second vocable");
        model.saveVocable(secondVocableToSave);
    }

    private void exportDatabaseOnSd() {
        if (this.view.getContext() != null) {
            DatabaseManager.getInstance(this.view.getContext()).exportDBOnSD();
        }
    }
}
