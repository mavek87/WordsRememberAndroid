package com.matteoveroni.wordsremember.dictionary;

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

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.items.VocableListViewAdapter;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import org.greenrobot.eventbus.EventBus;

/**
 * Fragment that lists all the vocables in the dictionary using getSelectedVocable Cursor Loader.
 * Is also possible to remove vocables using getSelectedVocable long press touch.
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = TagGenerator.tag(DictionaryManagementFragment.class);
    private final EventBus eventBus = EventBus.getDefault();
    private VocableListViewAdapter dictionaryListViewAdapter;

    public DictionaryManagementFragment() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Android lifecycle methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_management, container, false);
        dictionaryListViewAdapter = new VocableListViewAdapter(getContext(), null);
        setListAdapter(dictionaryListViewAdapter);
        getLoaderManager().initLoader(1, null, this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(1, null, this);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Cursor cursor = ((VocableListViewAdapter) listView.getAdapter()).getCursor();
        eventBus.postSticky(new EventVocableSelected(getSelectedVocable(cursor, position)));
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
        Cursor cursor = dictionaryListViewAdapter.getCursor();

        switch (item.getItemId()) {
            case R.id.menu_dictionary_management_long_press_remove:
                eventBus.postSticky(
                        new EventVocableManipulationRequest(
                                getSelectedVocable(cursor, position), EventVocableManipulationRequest.TypeOfManipulation.REMOVE
                        )
                );
                return true;
        }
        return super.onContextItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LoaderManager callbacks
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                VocablesContract.CONTENT_URI,
                VocablesContract.Schema.ALL_COLUMNS,
                null,
                null,
                VocablesContract.Schema.COLUMN_VOCABLE + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        dictionaryListViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dictionaryListViewAdapter.swapCursor(null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Word getSelectedVocable(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        return DictionaryDAO.cursorToVocable(cursor);
    }
}








