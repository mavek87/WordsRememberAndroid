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
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventCreateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.model.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Fragment designed to manage CRUD operations on single vocables in a dictionary
 *
 * @author Matteo Veroni
 */

public class DictionaryManipulationFragment extends Fragment {

    // ATTRIBUTES

    public static final String TAG = "F_DICTIONARY_MANIPULATION";

    private TextView lbl_title;
    private TextView lbl_vocableName;

    private ManipulationMode mode;

    private enum ManipulationMode {
        CREATE, UPDATE, READONLY_VIEW;
    }

    /**********************************************************************************************/

    // CONSTRUCTORS

    /**
     * Empty Constructor
     */
    public DictionaryManipulationFragment() {
    }

    /**********************************************************************************************/

    // ANDROID LIFECYCLE METHODS

    /**
     * Method called when this fragment is attached to an activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    /**
     * Method called when this fragment is detached from an activity
     */
    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    /**
     * Method called during the view creation
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary_manipulation_vocable, container, false);
    }

    /**
     * Method called when view is created
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lbl_title = (TextView) getActivity().findViewById(R.id.fragment_dictionary_manipulation_title);
        lbl_vocableName = (TextView) getActivity().findViewById(R.id.fragment_dictionary_manipulation_lbl_vocable_name);

        lbl_title.setText("Manipulate Vocable");
    }

    /**********************************************************************************************/

    // EVENTS

    /**
     * Capture event notified when when a vocable is selected/deselected
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventInformObserversOfItemSelected(EventNotifySelectedVocableToObservers event) {
        if (isViewCreated()) {
            // Get data from event
            Word selectedVocable = event.getSelectedVocable();
            // Populate view with data
            populateViewUsingData(selectedVocable);
            // Consume the event
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    /**
     * Set the fragment to handle vocable creation use case
     *
     * @param event
     */
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventInformObserversOfItemSelected(EventCreateVocable event) {
        if (isViewCreated()) {
            // Change fragment's title
            lbl_title.setText("Create new vocable");
            // Set fragment's mode
            mode = ManipulationMode.CREATE;
            // Consume the event
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

    /**********************************************************************************************/

    // HELPER METHODS

    /**
     * Helper method to populate view using data
     *
     * @param vocable
     */
    private void populateViewUsingData(Word vocable) {
        if (vocable != null && vocable.getName() != null) {
            lbl_vocableName.setText(vocable.getName());
        } else {
            lbl_vocableName.setText("");
        }
    }

    /**
     * Checks whether the view is created or not
     * @return isViewCreated boolean
     */
    private boolean isViewCreated() {
        return getView() != null && lbl_title != null && lbl_vocableName != null;
    }

    /**********************************************************************************************/
}
