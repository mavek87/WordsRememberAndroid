package com.matteoveroni.wordsremember.models;

import android.view.View;

import com.matteoveroni.wordsremember.ui.DictionaryManagementActivityView;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementActivityPresenter {

    private DictionaryDAO model;
    private View view;

    public void DictionaryManagementActivityPresenter(DictionaryManagementActivityView view) {

        this.view = (View) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{View.class},
                new ProxyNoOpIfNullObject(view));
    }



}
