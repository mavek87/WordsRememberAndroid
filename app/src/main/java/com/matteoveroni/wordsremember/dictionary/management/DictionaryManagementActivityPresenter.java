package com.matteoveroni.wordsremember.dictionary.management;

import com.matteoveroni.wordsremember.dictionary.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.models.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutChronology;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementActivityPresenter implements DictionaryManagementPresenter {

    private DictionaryManagementView view;
    private DictionaryDAO model;
    private DictionaryManagementViewLayoutManager layoutManager;

    @Override
    public void onViewAttached(Object view) {
        this.view = (DictionaryManagementView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManagementView.class},
                new NullWeakReferenceProxy(view));

        setupModelAndLayoutManagerIfNull();
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    @Override
    public boolean onKeyBackPressedRestorePreviousState() {
        boolean previousLayoutRestored;
        try {
            layoutManager.getViewLayout(ViewLayoutChronology.PREVIOUS_LAYOUT);
            previousLayoutRestored = true;
        } catch (Exception ex) {
            previousLayoutRestored = false;
        }
        return previousLayoutRestored;
    }

    @Override
    public void onViewCreatedForTheFirstTime() {
        view.useSingleLayoutWithFragment(DictionaryManagementFragment.TAG);
        layoutManager.saveLayoutInUse(view.getViewLayout());
    }

    @Override
    public void onViewRestored() {
        restoreSavedLayoutIfPresent();
    }

    private void setupModelAndLayoutManagerIfNull() {
        if (model == null && this.view.getContext() != null) {
            model = new DictionaryDAO(this.view.getContext());
            populateDatabase();
            exportDatabaseOnSd();
        }
        if (layoutManager == null) {
            layoutManager = new DictionaryManagementViewLayoutManager();
        }
    }

    private void restoreSavedLayoutIfPresent() {
        try {
            ViewLayout viewLayout = layoutManager.getViewLayout(ViewLayoutChronology.CURRENT_LAYOUT);
            switch (viewLayout.getViewLayoutType()) {
                case SINGLE_LAYOUT:
                    view.useSingleLayoutWithFragment(viewLayout.getMainFragmentTAG());
                    break;
                case TWO_COLUMNS_LAYOUT:
                    view.useTwoHorizontalColumnsLayout();
                    break;
                case TWO_ROWS_LAYOUT:
                    view.useTwoVerticalRowsLayout();
                    break;
            }
        } catch (Exception ex) {
            view.useSingleLayoutWithFragment(DictionaryManagementFragment.TAG);
            layoutManager.saveLayoutInUse(this.view.getViewLayout());
        }
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
