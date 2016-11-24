package com.matteoveroni.wordsremember.activities.dictionary_management.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventDictionaryItemSelected;
import com.matteoveroni.wordsremember.items.WordListViewAdapter;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

import org.greenrobot.eventbus.EventBus;

/**
 * List Fragment that shows all the vocables in the dictionary and allows to edit them
 * using a long press touch.
 *
 * @author Matteo Veroni
 */
public class DictionaryManagementFragment
        extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "F_DICTIONARY_MANAGEMENT";

    private WordListViewAdapter dictionaryListViewAdapter;

    public DictionaryManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_management, container, false);
        getLoaderManager().initLoader(0, null, this);
        setupDictionaryAdapter();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                DictionaryProvider.CONTENT_URI,
                DictionaryContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        dictionaryListViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dictionaryListViewAdapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set the list choice mode to allow only one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        dictionaryListViewAdapter.setSelected(position, !dictionaryListViewAdapter.isSelected(position));


        EventBus.getDefault().post(new EventDictionaryItemSelected(id));

        dictionaryListViewAdapter.notifyDataSetChanged();
    }

    private void setupDictionaryAdapter() {
        dictionaryListViewAdapter = new WordListViewAdapter(getContext(), null);
        setListAdapter(dictionaryListViewAdapter);
    }
}








