package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableUpdated;
import com.matteoveroni.wordsremember.dictionary.events.EventResetDictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocable;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager.ViewLayoutChronology;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementActivityPresenter implements Presenter {

    public static final String TAG = "DictManagePresenter";

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private final ViewLayoutManager viewLayoutManager;
    private DictionaryManagementView view;

    public DictionaryManagementActivityPresenter(DictionaryDAO model, ViewLayoutManager viewLayoutManager) {
        this.model = model;
        this.viewLayoutManager = viewLayoutManager;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManagementView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManagementView.class},
                new NullWeakReferenceProxy(viewAttached)
        );

        eventBus.register(this);
    }

    @Override
    public void onViewDetached() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    public void onViewRestored() {
        restoreSavedViewLayoutIfPresent(ViewLayoutChronology.LAST_LAYOUT);
    }

    public void onViewCreatedForTheFirstTime() {
        view.useSingleLayoutWithFragment(DictionaryManagementFragment.TAG);
        viewLayoutManager.saveLayoutInUse(view.getViewLayout());
        populateDatabaseForTestPurposes();
    }

    public boolean onKeyBackPressedRestorePreviousState() {
        return restoreSavedViewLayoutIfPresent(ViewLayoutChronology.PREVIOUS_LAYOUT);
    }

    public void onCreateVocableRequest() {
        showManipulationViewUsingVocable(null);
    }

    public void onSaveVocableRequest(Word vocable) {
        model.asyncSaveVocable(vocable);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventResetDictionaryManagementView(EventResetDictionaryManagementView event) {
        eventBus.removeStickyEvent(event);
        onViewCreatedForTheFirstTime();
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventVocableSelected(EventVocableSelected event) {
        Word selectedVocable = event.getSelectedVocable();
        showManipulationViewUsingVocable(selectedVocable);
        eventBus.removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncSaveVocableCompleted(EventAsyncSaveVocable event) {
        eventBus.removeStickyEvent(event);
        long savedVocableId = event.getIdOfInsertedVocable();
        view.showMessage("saved with id " + savedVocableId);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncUpdatedVocableCompleted(EventVocableUpdated event) {
        eventBus.removeStickyEvent(event);
    }

    private void showManipulationViewUsingVocable(Word vocable) {
        try {
            if (!view.isViewLarge()) {
                view.goToManipulationView(vocable);
            } else {
                determinateAndApplyLayoutForView(DictionaryManipulationFragment.TAG);
                viewLayoutManager.saveLayoutInUse(view.getViewLayout());
//                eventBus.postSticky(new EventVisualizeVocable(vocable));
            }
        } catch (ViewLayoutManager.NoViewLayoutFoundException ex) {
            //
        }
    }

    private boolean restoreSavedViewLayoutIfPresent(ViewLayoutChronology viewLayoutChronology) {
        ViewLayout viewLayoutToRestore;
        try {
            viewLayoutToRestore = viewLayoutManager.getLayout(viewLayoutChronology);
            switch (viewLayoutToRestore.getViewLayoutType()) {
                case SINGLE_LAYOUT:
                    view.useSingleLayoutWithFragment(viewLayoutToRestore.getMainFragmentTAG());
                    break;
                case TWO_COLUMNS_LAYOUT:
                    if (view.isViewLarge()) {
                        viewLayoutManager.removeLastLayoutSaved();
                        determinateAndApplyLayoutForView(null);
                    } else {
                        view.useTwoHorizontalColumnsLayout();
                    }
                    break;
                case TWO_ROWS_LAYOUT:
                    if (view.isViewLarge()) {
                        viewLayoutManager.removeLastLayoutSaved();
                        determinateAndApplyLayoutForView(null);
                    } else {
                        view.useTwoVerticalRowsLayout();
                    }
                    break;
            }
            viewLayoutManager.saveLayoutInUse(view.getViewLayout());
        } catch (ViewLayoutManager.NoViewLayoutFoundException e) {
            return false;
        }
        return true;
    }

    private void determinateAndApplyLayoutForView(String fragmentTAG) {
        if (view.isViewLarge()) {
            if (view.isViewLandscape()) {
                view.useTwoHorizontalColumnsLayout();
            } else {
                view.useTwoVerticalRowsLayout();
            }
        } else {
            view.useSingleLayoutWithFragment(fragmentTAG);
        }
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
