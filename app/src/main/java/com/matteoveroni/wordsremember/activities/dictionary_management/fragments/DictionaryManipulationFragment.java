package com.matteoveroni.wordsremember.activities.dictionary_management.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.model.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationFragment extends Fragment {

    public static final String TAG = "F_DICTIONARY_MANIPULATION";

    private TextView lbl_vocableName;

    public DictionaryManipulationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary_manipulation_vocable, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lbl_vocableName = (TextView) getActivity().findViewById(R.id.fragment_dictionary_manipulation_lbl_vocable_name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventInformObserversOfItemSelected(EventNotifySelectedVocableToObservers event) {
        if (lbl_vocableName != null) {
            Word selectedVocable = event.getSelectedVocable();
            populateViewUsingData(selectedVocable);
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    private void populateViewUsingData(Word vocable) {
        if (vocable != null) {
            lbl_vocableName.setText(vocable.getName());
        } else {
            lbl_vocableName.setText("");
        }
    }

}
