package com.matteoveroni.wordsremember.dictionary.presenter;

import android.content.Context;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;

/**
 * @author Matteo Veroni
 */

public interface IDictionaryManagementPresenter extends Presenter{
    void onViewCreated(Context context);
    void onCreateVocableRequest();
    void onEvent(EventVocableSelected event);
    void onEvent(EventVocableManipulationRequest event);
    void onEvent(EventAsyncDeleteVocableCompleted event);
}
