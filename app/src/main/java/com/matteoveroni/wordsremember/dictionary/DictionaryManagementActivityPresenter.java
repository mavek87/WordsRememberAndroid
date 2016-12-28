package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventResetDictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutManager.ViewLayoutChronology;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;
import java.util.Random;

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

    /**********************************************************************************************/

    // Presenter interface

    /**********************************************************************************************/

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

    /**********************************************************************************************/

    // View's callbacks

    /**********************************************************************************************/

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

    public void onSaveRequest(Word currentVocableInView) {
        if (currentVocableInView != null && !currentVocableInView.getName().trim().isEmpty()) {
            if (currentVocableInView.getId() < 0) {
                model.asyncSaveVocable(currentVocableInView);
                // TODO: manage the layout
                return;
            } else if (currentVocableInView.getId() > 0) {
                model.asyncUpdateVocable(currentVocableInView.getId(), currentVocableInView);
                // TODO: manage the layout
                return;
            }
        }
        view.showMessage("Error occurred during the saving process. Compile all the data and retry");
    }

    /**********************************************************************************************/

    // System Events

    /**********************************************************************************************/

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventResetDictionaryManagementView event) {
        eventBus.removeStickyEvent(event);
        onViewCreatedForTheFirstTime();
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventVocableManipulationRequest event) {
        Word vocableToManipulate = event.getVocableToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                model.asyncDeleteVocable(vocableToManipulate.getId());
                break;
        }
        eventBus.removeStickyEvent(event);
        // TODO: post event to inform the listview adapter of data change
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncDeleteVocableCompleted event) {
        view.showMessage("Vocable removed");
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventVocableSelected event) {
        Word selectedVocable = event.getSelectedVocable();
        showManipulationViewUsingVocable(selectedVocable);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncSaveVocableCompleted event) {
        eventBus.removeStickyEvent(event);
        long savedVocableId = event.getIdOfSavedVocable();
        view.showMessage("saved with id " + savedVocableId);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncUpdateVocableCompleted event) {
        eventBus.removeStickyEvent(event);
    }

    /**********************************************************************************************/

    // Helper methods

    /**********************************************************************************************/

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
        view.showMessage("populateDatabaseForTestPurposes");

        Word firstVocableToSave = new Word(generateRandomWord());
        model.saveVocable(firstVocableToSave);

        Word secondVocableToSave = new Word(generateRandomWord());
        model.saveVocable(secondVocableToSave);
    }

    private String generateRandomWord() {
        final Random randomGenerator = new Random();
        final int MAX_DECIMALS = 10;
        String generatedWord = "";

        int generatedDecimals = randomGenerator.nextInt(MAX_DECIMALS);

        for (int i = 0; i < generatedDecimals; i++) {
            char randomChar = (char) (randomGenerator.nextInt(25) + 97);
            generatedWord += randomChar;
        }
        return generatedWord;
    }

//    private void exportDatabaseOnSd() {
//        if (MyApp.getContext() != null) {
//            DatabaseManager.create(this.view.getContext()).exportDBOnSD();
//        }
//    }


}
