package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 */

public class DictionaryManagementPresenter implements Presenter {

    public static final String TAG = "DictManagePresenter";

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryManagementView view;

    public DictionaryManagementPresenter(DictionaryDAO model) {
        this.model = model;
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

    void onViewCreatedForTheFirstTime() {
        view.showMessage("created for the first time");
        populateDatabaseForTestPurposes();
    }

    void onCreateVocableRequest() {
        view.goToManipulationView(null);
    }

    /**********************************************************************************************/

    // System Events

    /**********************************************************************************************/

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    /**
     * Event fired from DictionaryManagementFragment when a vocable is being selected
     */
    public void onEvent(EventVocableSelected event) {
        Word selectedVocable = event.getSelectedVocable();
        eventBus.removeStickyEvent(event);
        view.goToManipulationView(selectedVocable);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    /**
     * Event fired from DictionaryManagementFragment when a manipulation on a vocable is requested
     */
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
        eventBus.removeStickyEvent(event);
        view.showMessage("Vocable removed");
    }

    /**********************************************************************************************/

    // Helper methods

    /**********************************************************************************************/

    private void populateDatabaseForTestPurposes() {
        Word randomVocableToSave = new Word(generateRandomWord());
        model.saveVocable(randomVocableToSave);
    }

    private String generateRandomWord() {
        final Random randomGenerator = new Random();
        final int MAX_NUM_LETTERS_FOR_WORD = 30;
        final int MIN_NUM_LETTERS_FOR_WORD = 3;
        final int NUM_OF_ASCII_CHARACTERS = 25;
        final int FIRST_ASCII_LETTER = 97;
        String generatedWord = "";

        int numberOfLetters = (randomGenerator.nextInt(MAX_NUM_LETTERS_FOR_WORD - MIN_NUM_LETTERS_FOR_WORD)) + MIN_NUM_LETTERS_FOR_WORD;

        for (int letter = 0; letter < numberOfLetters; letter++) {
            char randomChar = (char) (randomGenerator.nextInt(NUM_OF_ASCII_CHARACTERS) + FIRST_ASCII_LETTER);
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
