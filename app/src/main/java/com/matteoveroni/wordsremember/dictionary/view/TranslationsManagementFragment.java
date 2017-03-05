package com.matteoveroni.wordsremember.dictionary.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.view.ViewPojoUser;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.ui.adapters.TranslationsListViewAdapter;
import com.matteoveroni.wordsremember.ui.adapters.VocableListViewAdapter;

/**
 * @author Matteo Veroni
 */

public class TranslationsManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ViewPojoUser<Word> {

    public static final String TAG = TagGenerator.tag(TranslationsManagementFragment.class);

    private final int CURSOR_LOADER_ID = 1;

    private TranslationsListViewAdapter translationsListViewAdapter;
    private Word vocableInView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translations_management, container, false);
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
        return vocableInView;
    }

    @Override
    public void setPojoUsedInView(Word vocable) {
        if (vocable == null) {
            throw new IllegalArgumentException("Error setting pojo for TranslationsManagementFragment. Vocable passed cannot be null!");
        }
        if (vocableInView == null) {
            vocableInView = vocable;
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        } else {
            vocableInView = vocable;
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = translationsListViewAdapter.getCursor();
        cursor.moveToPosition(position);

        //TODO: implement real behaviours clicking on translations
        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);
        Toast.makeText(getContext(), selectedTranslation.getName(), Toast.LENGTH_SHORT).show();
    }

    /***********************************************************************************************
     * LoaderManager callbacks
     **********************************************************************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.CONTENT_URI,
                null,
                null,
                new String[]{String.valueOf(vocableInView.getId())},
                TranslationsContract.Schema.COLUMN_TRANSLATION + " ASC"
        );
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
