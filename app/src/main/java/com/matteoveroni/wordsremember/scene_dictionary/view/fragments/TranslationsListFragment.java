package com.matteoveroni.wordsremember.scene_dictionary.view.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventTranslationManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventTranslationSelected;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.ui.listview.adapters.TranslationsListViewAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class TranslationsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, PojoManipulable<Word> {

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final int ID_CURSOR_LOADER = 1;

    public static final String TAG = TagGenerator.tag(TranslationsListFragment.class);
    public static final String FRAGMENT_TYPE_KEY = "fragment_type_key";

    public enum TranslationsType {
        TRANSLATIONS, TRANSLATIONS_FOR_VOCABLE, TRANSLATIONS_NOT_FOR_VOCABLE;
    }

    private TranslationsType fragTranslationsType = TranslationsType.TRANSLATIONS;
    private TranslationsListViewAdapter translationsListAdapter;
    private Unbinder butterknifeBinder;
    private Word currentVocable;

    @Override
    public Word getPojoUsed() {
        return currentVocable;
    }

    @Override
    public void setPojoUsed(Word vocable) {
        this.currentVocable = vocable;
    }

    @BindView(R.id.fragment_translations_list_title)
    TextView lbl_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_translations_list, container, false);
        butterknifeBinder = ButterKnife.bind(this, view);

        Bundle args = getArguments();
        fragTranslationsType = Json.getInstance().fromJson(args.getString(FRAGMENT_TYPE_KEY), TranslationsType.class);

        translationsListAdapter = new TranslationsListViewAdapter(getContext(), null);
        setListAdapter(translationsListAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        butterknifeBinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(ID_CURSOR_LOADER, getArguments(), this);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = translationsListAdapter.getCursor();
        cursor.moveToPosition(position);

        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);
        EVENT_BUS.post(new EventTranslationSelected(selectedTranslation));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (fragTranslationsType) {
            case TRANSLATIONS:
                lbl_title.setText(getString(R.string.translations));
                return new CursorLoader(
                        getContext(),
                        TranslationsContract.CONTENT_URI,
                        null,
                        null,
                        null,
                        TranslationsContract.Schema.COL_TRANSLATION + " ASC");
            case TRANSLATIONS_FOR_VOCABLE:
                lbl_title.setText(getString(R.string.translations_for_vocable));
                return new CursorLoader(
                        getContext(),
                        VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_URI,
                        null,
                        null,
                        new String[]{String.valueOf(currentVocable.getId())},
                        TranslationsContract.Schema.COL_TRANSLATION + " ASC");
            case TRANSLATIONS_NOT_FOR_VOCABLE:
                lbl_title.setText(getString(R.string.other_translations_available));
                final String vocableId = String.valueOf(currentVocable.getId());
                return new CursorLoader(
                        getContext(),
                        Uri.withAppendedPath(VocablesTranslationsContract.NOT_TRANSLATION_FOR_VOCABLE_CONTENT_URI, vocableId),
                        null,
                        null,
                        null,
                        null
                );
            default:
                final String error = "Error during onCreateLoader. Unknown fragmentType of fragment set.";
                Log.e(TAG, error);
                throw new RuntimeException(error);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        translationsListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        translationsListAdapter.swapCursor(null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (fragTranslationsType != TranslationsType.TRANSLATIONS_NOT_FOR_VOCABLE) {
            getActivity().getMenuInflater().inflate(R.menu.menu_dictionary_list_long_press, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int selectedPosition = contextMenuInfo.position;
        final Cursor cursor = translationsListAdapter.getCursor();
        switch (item.getItemId()) {
            case R.id.menu_dictionary_list_long_press_remove:
                cursor.moveToPosition(selectedPosition);
                Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);
                removeTranslationRequest(selectedTranslation);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void removeTranslationRequest(Word translation) {
        switch (fragTranslationsType) {
            case TRANSLATIONS:
                EVENT_BUS.post(new EventTranslationManipulationRequest(translation, TypeOfManipulationRequest.REMOVE));
                break;
            case TRANSLATIONS_FOR_VOCABLE:
                EVENT_BUS.post(new EventVocableTranslationManipulationRequest(currentVocable.getId(), translation.getId(), TypeOfManipulationRequest.REMOVE));
                break;
            default:
                throw new RuntimeException("Unexpected fragmentType");
        }
    }
}
