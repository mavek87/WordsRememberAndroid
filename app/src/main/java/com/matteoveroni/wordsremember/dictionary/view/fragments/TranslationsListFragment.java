package com.matteoveroni.wordsremember.dictionary.view.fragments;

import android.database.Cursor;
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

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationSelected;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.view.PojoManipulableView;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.ui.adapters.TranslationsListViewAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class TranslationsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, PojoManipulableView<Word> {

    public static final String TAG = TagGenerator.tag(TranslationsListFragment.class);

    private static final EventBus eventBus = EventBus.getDefault();

    private TranslationsListViewAdapter translationsListViewAdapter;

    private final int CURSOR_LOADER_ID = 1;

    public enum Type {
        ONLY_TRANSLATIONS, TRANSLATIONS_FOR_VOCABLE, TRANSLATIONS_NOT_FOR_VOCABLE;
    }

    public Type type = Type.ONLY_TRANSLATIONS;

    private Word vocableAssociatedToView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translations_list, container, false);
        translationsListViewAdapter = new TranslationsListViewAdapter(getContext(), null);
        setListAdapter(translationsListViewAdapter);
        return view;
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
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, getArguments(), this);
        super.onResume();
    }

    @Override
    public Word getPojoUsedByView() {
        return vocableAssociatedToView;
    }

    @Override
    public void setPojoUsedByView(Word vocable) {
        this.vocableAssociatedToView = vocable;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = translationsListViewAdapter.getCursor();
        cursor.moveToPosition(position);

        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);
        EventBus.getDefault().post(new EventTranslationSelected(selectedTranslation));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (type) {
            case ONLY_TRANSLATIONS:
                return getCursorForAllTheTranslations();
            case TRANSLATIONS_FOR_VOCABLE:
                return getCursorForAllTheTranslationsForVocable();
            case TRANSLATIONS_NOT_FOR_VOCABLE:
                return getCursorForAllTheTranslationsExceptThoseForVocable();
            default:
                final String error = "Error during onCreateLoader. Unknown type of fragment set.";
                Log.e(TAG, error);
                throw new RuntimeException(error);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        translationsListViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        translationsListViewAdapter.swapCursor(null);
    }

    private Loader<Cursor> getCursorForAllTheTranslations() {
        return new CursorLoader(
                getContext(),
                TranslationsContract.CONTENT_URI,
                null,
                null,
                null,
                TranslationsContract.Schema.COLUMN_TRANSLATION + " ASC");
    }

    private Loader<Cursor> getCursorForAllTheTranslationsForVocable() {
        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.CONTENT_URI,
                null,
                null,
                new String[]{String.valueOf(vocableAssociatedToView.getId())},
                TranslationsContract.Schema.COLUMN_TRANSLATION + " ASC");

    }

    private Loader<Cursor> getCursorForAllTheTranslationsExceptThoseForVocable() {
        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.CONTENT_URI,
                null,
                VocablesTranslationsContract.Schema.TABLE_DOT_COLUMN_VOCABLE_ID + "!=?",
                new String[]{String.valueOf(vocableAssociatedToView.getId())},
                TranslationsContract.Schema.COLUMN_TRANSLATION + " ASC");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (type != Type.TRANSLATIONS_NOT_FOR_VOCABLE) {
            getActivity().getMenuInflater().inflate(R.menu.menu_dictionary_list_long_press, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = contextMenuInfo.position;
        final Cursor cursor = translationsListViewAdapter.getCursor();
        switch (item.getItemId()) {
            case R.id.menu_dictionary_list_long_press_remove:
                Word selectedTranslation = getSelectedTranslation(cursor, position);
                removeTranslationAction(selectedTranslation);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private Word getSelectedTranslation(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        return DictionaryDAO.cursorToTranslation(cursor);
    }

    private void removeTranslationAction(Word translation) {
        switch (type) {
            case ONLY_TRANSLATIONS:
                eventBus.post(new EventTranslationManipulationRequest(translation, TypeOfManipulationRequest.REMOVE));
                eventBus.post(new EventVocableTranslationManipulationRequest(null, translation, TypeOfManipulationRequest.REMOVE));
                break;
            case TRANSLATIONS_FOR_VOCABLE:
                eventBus.post(new EventVocableTranslationManipulationRequest(vocableAssociatedToView, translation, TypeOfManipulationRequest.REMOVE));
                break;
            default:
                throw new RuntimeException("Unexpected type");
        }
    }
}
