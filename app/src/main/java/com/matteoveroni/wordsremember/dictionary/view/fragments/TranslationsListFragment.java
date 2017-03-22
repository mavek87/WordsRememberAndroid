package com.matteoveroni.wordsremember.dictionary.view.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationSelected;
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

    private TranslationsListViewAdapter translationsListViewAdapter;

    private final int CURSOR_LOADER_ID = 1;

    private Word vocableTranslated;

    public static final String KEY_TRANSLATIONS_FOR_VOCABLE = "TranslationsForVocable";
    public static final String KEY_TRANSLATIONS_NOT_FOR_VOCABLE = "TranslationsNotForVocable";

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
    public Word getPojoUsedByView() {
        return vocableTranslated;
    }

    @Override
    public void setPojoUsedInView(Word vocable) {
        vocableTranslated = vocable;
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, getArguments(), this);
        super.onResume();
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
        if (args == null || args.isEmpty()) {
            return getCursorForAllTheTranslations();
        } else if (args.containsKey(KEY_TRANSLATIONS_FOR_VOCABLE)) {
            return getCursorForAllTheTranslationsForVocable();
        } else if (args.containsKey(KEY_TRANSLATIONS_NOT_FOR_VOCABLE)) {
            return getCursorForAllTheTranslationsExceptThoseForVocable();
        } else {
            final String error = "Bundle not null or empty, but any known key passed to fragment during onCreateLoader";
            Log.e(TAG, error);
            throw new RuntimeException(error);
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getActivity().getMenuInflater().inflate(R.menu.menu_dictionary_list_long_press, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        int position = contextMenuInfo.position;
//        Cursor cursor = vocableListAdapter.getCursor();
//
//        switch (item.getItemId()) {
//
//            case R.id.menu_dictionary_list_long_press_remove:
//                Word selectedVocable = getSelectedVocable(cursor, position);
//                eventBus.post(
//                        new EventVocableManipulationRequest(selectedVocable, EventVocableManipulationRequest.TypeOfManipulation.REMOVE)
//                );
//                return true;
//
//        }
//        return super.onContextItemSelected(item);
//    }
//
//    private Word getSelectedVocable(Cursor cursor, int position) {
//        cursor.moveToPosition(position);
//        return DictionaryDAO.cursorToVocable(cursor);
//    }

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
                new String[]{String.valueOf(vocableTranslated.getId())},
                TranslationsContract.Schema.COLUMN_TRANSLATION + " ASC");

    }

    private Loader<Cursor> getCursorForAllTheTranslationsExceptThoseForVocable() {
        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.CONTENT_URI,
                null,
                VocablesTranslationsContract.Schema.TABLE_DOT_COLUMN_VOCABLE_ID + "!=?",
                new String[]{String.valueOf(vocableTranslated.getId())},
                TranslationsContract.Schema.COLUMN_TRANSLATION + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        translationsListViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        translationsListViewAdapter.swapCursor(null);
    }
}
