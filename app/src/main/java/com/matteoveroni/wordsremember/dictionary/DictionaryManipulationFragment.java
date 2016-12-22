package com.matteoveroni.wordsremember.dictionary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.EventStartVocableCreation;
import com.matteoveroni.wordsremember.dictionary.events.EventVisualizeVocable;
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

    private final EventBus eventBus = EventBus.getDefault();

    @BindView(R.id.fragment_dictionary_manipulation_title)
    TextView lbl_title;

    @BindView(R.id.fragment_dictionary_manipulation_lbl_vocable_name)
    TextView lbl_vocableName;

    private DictionaryManipulationMode fragmentMode;

    private enum DictionaryManipulationMode {
        VIEW, CREATE, UPDATE;
    }

    private Unbinder viewInjector;

    public DictionaryManipulationFragment() {
    }

    // ANDROID LIFECYCLE METHODS
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventBus.register(this);
    }

    @Override
    public void onDetach() {
        eventBus.unregister(this);
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

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventNotifiedVocableToVisualize(EventVisualizeVocable event) {
        if (isViewCreated()) {
            Word selectedVocable = event.getVocable();
            populateViewUsingData(selectedVocable);
        }
        eventBus.removeStickyEvent(event);
    }

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventVocableCreationRequest(EventStartVocableCreation event) {
        eventBus.removeStickyEvent(event);
        if (isViewCreated()) {
            populateViewUsingData(null);
            fragmentMode = DictionaryManipulationMode.CREATE;
        }
    }

    private void populateViewUsingData(Word vocable) {
        if (vocable == null || vocable.getName() == null) {
            lbl_title.setText("Create vocable");
            lbl_vocableName.setText("");
            fragmentMode = DictionaryManipulationMode.CREATE;
        } else {
            lbl_title.setText("View vocable");
            lbl_vocableName.setText(vocable.getName());
            fragmentMode = DictionaryManipulationMode.VIEW;
        }
    }

    private boolean isViewCreated() {
        return getView() != null && lbl_title != null && lbl_vocableName != null;
    }
}
