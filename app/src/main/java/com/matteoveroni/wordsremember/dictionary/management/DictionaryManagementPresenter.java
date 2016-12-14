package com.matteoveroni.wordsremember.dictionary.management;

import android.widget.Toast;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.models.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutChronology;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementPresenter implements Presenter {

    private DictionaryManagementView view;

    private final DictionaryDAO model;
    private final DictionaryManagementViewLayoutManager layoutManager;

    public DictionaryManagementPresenter(DictionaryDAO model, DictionaryManagementViewLayoutManager layoutManager) {
        this.model = model;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManagementView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManagementView.class},
                new NullWeakReferenceProxy(viewAttached));

        Toast.makeText(view.getContext(), "modelloNullo = " + (model == null) + " TAG = " + model.TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void onViewDestroyed() {
        view = null;
    }

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

    public void onViewCreatedForTheFirstTime() {
//        view.useSingleLayoutWithFragment(DictionaryManagementFragment.TAG);
//        injectedLayoutManager.saveLayoutInUse(view.getViewLayout());
    }

    public void onViewRestored() {
        restoreSavedLayoutIfPresent();
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

    private void populateDatabaseForTestPurposes() {
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
