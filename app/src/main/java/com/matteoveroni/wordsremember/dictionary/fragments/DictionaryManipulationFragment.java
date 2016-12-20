package com.matteoveroni.wordsremember.dictionary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.events.EventVocableCreationRequest;
import com.matteoveroni.wordsremember.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment designed to manage CRUD operations on single vocables in a dictionary
 *
 * @author Matteo Veroni
 */

public class DictionaryManipulationFragment extends Fragment {

    public static final String TAG = "F_DICTIONARY_MANIPULATION";

    private final static String TITLE_CONTENT_KEY = "TITLE_KEY";
    private final static String VOCABLE_NAME_CONTENT_KEY = "VOCABLE_NAME_KEY";

    @BindView(R.id.fragment_dictionary_manipulation_title)
    TextView lbl_title;

    @BindView(R.id.fragment_dictionary_manipulation_lbl_vocable_name)
    TextView lbl_vocableName;

    private DictionaryManipulationMode mode;

    private enum DictionaryManipulationMode {
        CREATE, UPDATE;
    }

    private Unbinder viewInjector;

    /**********************************************************************************************/

    // CONSTRUCTORS

    /**
     * Empty Constructor
     */
    public DictionaryManipulationFragment() {
    }

    /**********************************************************************************************/

    // ANDROID LIFECYCLE METHODS
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
        View view = inflater.inflate(R.layout.fragment_dictionary_manipulation_vocable, container, false);
        viewInjector = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        viewInjector.unbind();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TITLE_CONTENT_KEY, lbl_title.getText().toString());
        savedInstanceState.putString(VOCABLE_NAME_CONTENT_KEY, lbl_vocableName.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TITLE_CONTENT_KEY)) {
                lbl_title.setText(savedInstanceState.getString(TITLE_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VOCABLE_NAME_CONTENT_KEY)) {
                lbl_vocableName.setText(savedInstanceState.getString(VOCABLE_NAME_CONTENT_KEY));
            }
        }
    }

    /**********************************************************************************************/

    // EVENTS

    /**
     * Capture event notified when when a vocable is selected/deselected
     *
     * @param event
     */
    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventNotifiedVocableToVisualize(EventNotifySelectedVocableToObservers event) {
        if (isViewCreated()) {
            Word selectedVocable = event.getSelectedVocable();
            populateViewUsingData("Edit Vocable", selectedVocable);
        }
    }

    /**
     * Method for handling vocable creation use case
     *
     * @param event
     */
    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventVocableCreationRequest(EventVocableCreationRequest event) {
        if (isViewCreated()) {
            populateViewUsingData("Create Vocable", null);
            mode = DictionaryManipulationMode.CREATE;
        }
    }

    /**********************************************************************************************/

    // HELPER METHODS

    /**
     * Helper method to populate view using data
     *
     * @param vocable
     */
    private void populateViewUsingData(String viewTitle, Word vocable) {
        lbl_title.setText(viewTitle);
        if (vocable != null && vocable.getName() != null) {
            lbl_vocableName.setText(vocable.getName());
        } else {
            lbl_vocableName.setText("");
        }
    }

    /**
     * Checks whether the view is created or not
     *
     * @return isViewCreated boolean
     */
    private boolean isViewCreated() {
        return getView() != null && lbl_title != null && lbl_vocableName != null;
    }

    /**********************************************************************************************/
}
