package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocable;
import com.matteoveroni.wordsremember.dictionary.events.EventResetDictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncGetVocableById;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocable;
import com.matteoveroni.wordsremember.dictionary.events.EventVisualizeVocable;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager.ViewLayoutChronology;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementPresenter implements Presenter {

    public static final String TAG = "DictManagePresenter";

    private final DictionaryDAO model;
    private final ViewLayoutManager viewLayoutManager;
    private DictionaryManagementView view;

    public DictionaryManagementPresenter(DictionaryDAO model, ViewLayoutManager viewLayoutManager) {
        this.model = model;
        this.viewLayoutManager = viewLayoutManager;
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

    public void onCreateVocableRequest(Word vocable) {
        model.asyncSaveVocable(vocable);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventResetDictionaryManagementView(EventResetDictionaryManagementView event) {
        onViewCreatedForTheFirstTime();
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
    public void onEventAsyncGetVocableByIdCompleted(EventAsyncGetVocableById event) {
        Word retrievedVocable = event.getVocableRetrieved();
        if (retrievedVocable != null) {
            try {
                ViewLayout viewLayout = viewLayoutManager.getLayout(ViewLayoutChronology.LAST_LAYOUT);
                if (viewLayout.getViewLayoutType().equals(ViewLayoutType.SINGLE_LAYOUT)) {
                    view.goToManipulationView(retrievedVocable);
                } else {
                    resolveAndApplyLayoutForView(DictionaryManipulationFragment.TAG);
                    EventBus.getDefault().postSticky(new EventVisualizeVocable(retrievedVocable));
                }
            }catch (ViewLayoutManager.NoViewLayoutFoundException ex){
            }
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncSaveVocableCompleted(EventAsyncSaveVocable event) {
        long savedVocableId = event.getIdOfInsertedVocable();
        view.showMessage("saved with id " + savedVocableId);
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncUpdatedVocableCompleted(EventAsyncUpdateVocable event) {
        view.showMessage("updated");
        EventBus.getDefault().removeStickyEvent(event);
    }

    private boolean restoreSavedViewLayoutIfPresent(ViewLayoutChronology viewLayoutChronology) {
        ViewLayout viewLayoutRestored = null;
        try {
            viewLayoutRestored = viewLayoutManager.getLayout(viewLayoutChronology);
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

    private void resolveAndApplyLayoutForView(String fragmentTAG) {
        if (view.isViewLarge()) {
            if (view.isViewLandscape()) {
                view.useTwoHorizontalColumnsLayout();
            } else {
                view.useTwoVerticalRowsLayout();
            }
        } else {
            view.useSingleLayoutWithFragment(fragmentTAG);
        }
        viewLayoutManager.saveLayoutInUse(view.getViewLayout());
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
