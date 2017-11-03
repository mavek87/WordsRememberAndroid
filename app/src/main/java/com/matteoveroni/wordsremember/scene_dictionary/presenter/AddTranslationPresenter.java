package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventTranslationSelected;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.LocaleKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenter implements Presenter {

    public static final String TAG = TagGenerator.tag(AddTranslationPresenter.class);

    protected static final int EDIT_TRANSLATION_REQUEST_CODE = 0;

    private final EventBus EVENT_BUS = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final DictionaryModel model;
    private AddTranslationView view;

    public AddTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (AddTranslationView) view;
        this.view.setPojoUsed(model.getVocableSelected());
        EVENT_BUS.register(this);
    }

    @Override
    public void detachView() {
        EVENT_BUS.unregister(this);
        view = null;
    }

    @Subscribe
    public void onEvent(EventTranslationSelected event) {
        Word translation = event.getSelectedTranslation();
        model.setTranslationSelected(translation);
        view.showMessage(new FormattedString("%s (" + translation.getName() + ")", LocaleKey.TRANSLATION_ADDED));
        view.returnToPreviousView();
    }

    // TODO: check if this method is useful
    @Subscribe
    public void onEvent(EventVocableTranslationManipulationRequest event) {
        final long translationId = event.getTranslationIdToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocableTranslationsByTranslationId(translationId);
                break;
        }
    }

    public void onCreateTranslationRequest() {
        view.switchToView(View.Name.EDIT_TRANSLATION, EDIT_TRANSLATION_REQUEST_CODE);
    }
}