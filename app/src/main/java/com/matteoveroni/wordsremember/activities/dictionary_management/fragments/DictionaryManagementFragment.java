package com.matteoveroni.wordsremember.activities.dictionary_management.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventManipulateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventVocableSelected;
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

    private long lastSelectedVocableID = -1;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set the list choice mode to allow only one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        registerForContextMenu(getListView());
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_dictionary_management_long_press, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = contextMenuInfo.position;

        long targetVocableID = dictionaryListViewAdapter.getItemId(position);

        switch (item.getItemId()) {
            case R.id.menu_dictionary_management_long_press_edit:
                EventBus.getDefault().postSticky(new EventManipulateVocable(targetVocableID, EventManipulateVocable.TypeOfManipulation.EDIT));
                return true;
            case R.id.menu_dictionary_management_long_press_remove:
                EventBus.getDefault().postSticky(new EventManipulateVocable(targetVocableID, EventManipulateVocable.TypeOfManipulation.REMOVE));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long selectedVocableID) {
        super.onListItemClick(l, v, position, selectedVocableID);

        if (selectedVocableID >= 0) {
            if (selectedVocableID != lastSelectedVocableID) {
                dictionaryListViewAdapter.setSelected(position, true);
                lastSelectedVocableID = selectedVocableID;
            } else {
                dictionaryListViewAdapter.setSelected(position, false);
                lastSelectedVocableID = -1;
            }
            Toast.makeText(getActivity(), "lastSelectedVocableID " + lastSelectedVocableID, Toast.LENGTH_SHORT).show();

            EventBus.getDefault().post(new EventVocableSelected(lastSelectedVocableID));
            dictionaryListViewAdapter.notifyDataSetChanged();
        }
    }

    private void setupDictionaryAdapter() {
        dictionaryListViewAdapter = new WordListViewAdapter(getContext(), null);
        setListAdapter(dictionaryListViewAdapter);
    }
}








