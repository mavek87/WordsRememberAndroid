package com.matteoveroni.wordsremember.dictionary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.utilities.Json;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment designed to manage CRUD operations on single vocables in a dictionary
 *
 * @author Matteo Veroni
 */

public class DictionaryManipulationFragment extends Fragment {

    public static final String TAG = TagGenerator.tag(DictionaryManipulationFragment.class);

    private final EventBus eventBus = EventBus.getDefault();
    private final static String VOCABLE_CONTENT_KEY = "VOCABLE_CONTENT_KEY";
    private final static String VIEW_TITLE_CONTENT_KEY = "VIEW_TITLE_CONTENT_KEY";
    private final static String VIEW_VOCABLE_NAME_CONTENT_KEY = "VIEW_VOCABLE_NAME_CONTENT_KEY";
    private Unbinder viewInjector;

    private Word shownVocable;

//    private TranslationsManagementFragment translationsManagementFragment;

    @BindView(R.id.fragment_dictionary_manipulation_title)
    TextView lbl_title;

    @BindView(R.id.fragment_dictionary_manipulation_txt_vocable_name)
    EditText txt_vocableName;

    public DictionaryManipulationFragment() {
    }

    public Word getCurrentVocableInView() {
        final Word currentVocableInView = new Word(txt_vocableName.getText().toString());
        currentVocableInView.setId(shownVocable.getId());
        return currentVocableInView;
    }

    public void showVocableData(Word vocableToShow) {
        if (!shownVocable.equals(vocableToShow)) {
            if (vocableToShow.getId() < 0) {
                lbl_title.setText("Create vocable");
            } else {
                lbl_title.setText("Edit vocable");
            }
            txt_vocableName.setText(vocableToShow.getName());
            shownVocable = vocableToShow;
//            passVocableInUseToTranslationsManagementFragment(vocableToShow);
        }
    }

    /**********************************************************************************************/

    // System events

    /**********************************************************************************************/

//    @Subscribe(sticky = true)
//    @SuppressWarnings("unused")
//    public void onEvent(EventVocableSelected event) {
//        if (isFragmentCreated()) {
//            Word vocableToShowInView = event.getSelectedVocable();
//            showVocableData(vocableToShowInView);
//        }
//    }
    @Override
    public void onResume() {
        super.onResume();
//        eventBus.register(this);
    }

    @Override
    public void onPause() {
//        eventBus.unregister(this);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_manipulation, container, false);
        viewInjector = ButterKnife.bind(this, view);
//        translationsManagementFragment = (TranslationsManagementFragment) getFragmentManager().findFragmentById(R.id.translations_management_fragment);
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
        savedInstanceState.putString(VOCABLE_CONTENT_KEY, Json.getInstance().toJson(shownVocable));
        savedInstanceState.putString(VIEW_TITLE_CONTENT_KEY, lbl_title.getText().toString());
        savedInstanceState.putString(VIEW_VOCABLE_NAME_CONTENT_KEY, txt_vocableName.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(VOCABLE_CONTENT_KEY)) {
                shownVocable = Json.getInstance().fromJson(savedInstanceState.getString(VOCABLE_CONTENT_KEY), Word.class);
            }
            if (savedInstanceState.containsKey(VIEW_TITLE_CONTENT_KEY)) {
                lbl_title.setText(savedInstanceState.getString(VIEW_TITLE_CONTENT_KEY));
            }
            if (savedInstanceState.containsKey(VIEW_VOCABLE_NAME_CONTENT_KEY)) {
                txt_vocableName.setText(savedInstanceState.getString(VIEW_VOCABLE_NAME_CONTENT_KEY));
            }
        }
    }

    private boolean isFragmentCreated() {
        return getView() != null && lbl_title != null && txt_vocableName != null;
    }

//    private void passVocableInUseToTranslationsManagementFragment(Word vocableToShow) {
//        Bundle bundle = new Bundle();
//        bundle.putString("vocableInUse", Json.getInstance().toJson(vocableToShow));
//        translationsManagementFragment.setArguments(bundle);
//    }
}
