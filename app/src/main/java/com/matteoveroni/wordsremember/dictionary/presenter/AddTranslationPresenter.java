package com.matteoveroni.wordsremember.dictionary.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationSelected;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenter implements Presenter {

    public static final String TAG = TagGenerator.tag(AddTranslationPresenter.class);
    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final DictionaryModel model;
    private AddTranslationView view;

    private static final String MSG_TRANSLATION_ADDED = "Translation added";
    private static final String UNSUPPORTED_TRANSLATION_MANIPULATION_EXCEPTION = "Unsupported vocable manipulation exception";

    public AddTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (AddTranslationView) view;
        Word newEmptyTranslation = new Word("");
        this.view.setPojoUsedByView(new VocableTranslation(model.getLastValidVocableSelected(), newEmptyTranslation));
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Subscribe
    public void onEvent(EventTranslationSelected event) {
        Word translation = event.getSelectedTranslation();
        model.setLastValidTranslationSelected(translation);
        view.showMessage(MSG_TRANSLATION_ADDED + " (" + translation.getName() + ")");
        view.returnToPreviousView();
    }

    @Subscribe
    public void onEvent(EventVocableTranslationManipulationRequest event) {
        final Word translation = event.getTranslationToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocableTranslationsByTranslationId(translation.getId());
                break;
            default:
                Log.e(TAG, UNSUPPORTED_TRANSLATION_MANIPULATION_EXCEPTION);
                throw new UnsupportedOperationException(UNSUPPORTED_TRANSLATION_MANIPULATION_EXCEPTION);
        }
    }

}
