package com.matteoveroni.wordsremember.dictionary.interfaces;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventSaveVocableRequest;
import com.matteoveroni.wordsremember.pojo.Word;

public interface DictionaryManipulationPresenter extends Presenter {

    void onVocableToManipulateLoaded(Word vocableToManipulate);
    void onEventSaveVocableRequest(EventSaveVocableRequest event);
}
