package com.matteoveroni.wordsremember.dictionary.management;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableSuccessful;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.dictionary.fragments.DictionaryManipulationFragment;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncGetVocableByIdSuccessful;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableSuccessful;
import com.matteoveroni.wordsremember.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager.ViewLayoutBackupChronology;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementActivityPresenter implements DictionaryManagementPresenter {

    public static final String TAG = "DictManagePresenter";

    private DictionaryManagementView view;

    private final DictionaryDAO model;
    private final DictionaryManagementViewLayoutManager layoutManager;

    public DictionaryManagementActivityPresenter(DictionaryDAO model, DictionaryManagementViewLayoutManager layoutManager) {
        this.model = model;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManagementView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManagementView.class},
                new NullWeakReferenceProxy(viewAttached));

        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewDetached() {
        view = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    @Override
    public void onViewRestored() {
        restoreSavedViewLayoutIfPresent(ViewLayoutBackupChronology.LAST_LAYOUT);
    }

    @Override
    public void onViewCreatedForTheFirstTime() {
        view.useSingleLayoutWithFragment(DictionaryManagementFragment.TAG);
        layoutManager.saveLayoutInUse(view.getViewLayout());
        populateDatabaseForTestPurposes();
    }

    @Override
    public boolean onKeyBackPressedRestorePreviousState() {
        return restoreSavedViewLayoutIfPresent(ViewLayoutBackupChronology.PREVIOUS_LAYOUT);
    }

    @Override
    public void onCreateVocableRequest(Word vocable) {
        model.asyncSaveVocable(vocable);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventVocableSelected(EventVocableSelected event) {
        long selectedVocableID = event.getSelectedVocableID();
        model.asyncGetVocableById(selectedVocableID);
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncGetVocableByIdSuccessful(EventAsyncGetVocableByIdSuccessful event) {
        Word retrievedVocable = event.getVocableRetrieved();
        if (retrievedVocable != null) {
            setLayoutForView(DictionaryManipulationFragment.TAG);
            EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(retrievedVocable));
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncSaveVocableSuccessful(EventAsyncSaveVocableSuccessful event) {
        long savedVocableId = event.getIdOfInsertedVocable();
        view.showMessage("saved with id " + savedVocableId);
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncUpdatedVocableSuccessful(EventAsyncUpdateVocableSuccessful event) {
        view.showMessage("updated");
        EventBus.getDefault().removeStickyEvent(event);
    }

    private boolean restoreSavedViewLayoutIfPresent(ViewLayoutBackupChronology viewLayoutBackupChronology) {
        ViewLayout viewLayoutRestored = null;
        try {
            viewLayoutRestored = layoutManager.getViewLayout(viewLayoutBackupChronology);
        } catch (ViewLayoutManager.NoViewLayoutFoundException ex) {
        }
        if (viewLayoutRestored != null) {
            switch (viewLayoutRestored.getViewLayoutType()) {
                case SINGLE_LAYOUT:
                    view.useSingleLayoutWithFragment(viewLayoutRestored.getMainFragmentTAG());
                    break;
                case TWO_COLUMNS_LAYOUT:
                    view.useTwoHorizontalColumnsLayout();
                    break;
                case TWO_ROWS_LAYOUT:
                    view.useTwoVerticalRowsLayout();
                    break;
            }
            return true;
        }
        return false;
    }

    private void setLayoutForView(String fragmentTAG) {
        if (view.isViewLarge()) {
            if (view.isViewLandscape()) {
                view.useTwoHorizontalColumnsLayout();
            } else {
                view.useTwoVerticalRowsLayout();
            }
        } else {
            view.useSingleLayoutWithFragment(fragmentTAG);
        }
        layoutManager.saveLayoutInUse(view.getViewLayout());
    }

    private void populateDatabaseForTestPurposes() {
        Word firstVocableToSave = new Word("test123");
        model.saveVocable(firstVocableToSave);

        Word secondVocableToSave = new Word("second vocable");
        model.saveVocable(secondVocableToSave);
    }

//    private void exportDatabaseOnSd() {
//        if (MyApp.getContext() != null) {
//            DatabaseManager.create(this.view.getContext()).exportDBOnSD();
//        }
//    }


}
