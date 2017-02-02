package com.matteoveroni.wordsremember.dictionary.presenter;

import android.content.Context;
import android.util.Log;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManagementView;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryManagementView view;

    public DictionaryManagementPresenter(DictionaryDAO model) {
        this.model = model;
    }

    private static boolean firstTimeCreated = true;

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

    // Activity's callbacks

    /**********************************************************************************************/

    public void onViewCreated(Context context) {
        if (firstTimeCreated) {
            populateDatabaseForTestPurposes(context);
            exportDatabaseOnSd(context);
            firstTimeCreated = false;
        }
    }

    public void onCreateVocableRequest() {
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

    private void populateDatabaseForTestPurposes(Context context) {
//        DatabaseManager.getInstance(context).resetDatabase();

        Map<String, Word> uniqueVocables = new HashMap<>();
        Word vocableToSave = null;
        for (int i = 0; i < 5; i++) {
            do {
                vocableToSave = new Word(generateRandomWord());
            } while (uniqueVocables.containsKey(vocableToSave.getName()));
            uniqueVocables.put(vocableToSave.getName(), vocableToSave);
            model.asyncSaveVocable(vocableToSave);
        }
    }

    private String generateRandomWord() {
        final Random randomGenerator = new SecureRandom();
        final int MAX_NUM_LETTERS = 30;
        final int MIN_NUM_LETTERS = 3;
        final int FIRST_ASCII_CHAR_CODE = 97;
        final int ASCII_CHAR_CODE = 25;
        String generatedWord = "";

        // MIN_NUM_LETTERS <= numbersOfLetters <= MAX_NUM_LETTERS
        int numberOfLetters = (randomGenerator.nextInt(MAX_NUM_LETTERS - MIN_NUM_LETTERS)) + MIN_NUM_LETTERS;

        for (int letter = 0; letter < numberOfLetters; letter++) {
            char randomChar = (char) (FIRST_ASCII_CHAR_CODE + randomGenerator.nextInt(ASCII_CHAR_CODE));
            generatedWord += randomChar;
        }
        return generatedWord;
    }

    private void exportDatabaseOnSd(Context context) {
        if (context != null) {
            DatabaseManager.getInstance(context).exportDBOnSD();
        }
    }
}
