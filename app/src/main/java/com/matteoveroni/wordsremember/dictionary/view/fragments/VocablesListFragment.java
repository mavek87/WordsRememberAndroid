package com.matteoveroni.wordsremember.dictionary.view.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.ui.adapters.VocableListViewAdapter;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Fragment that lists all the vocables in the dictionary using getSelectedVocable Cursor Loader.
 * Is also possible to remove vocables using getSelectedVocable long press touch.
 *
 * @author Matteo Veroni
 */

public class VocablesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final EventBus eventBus = EventBus.getDefault();
    private final int CURSOR_LOADER_ID = 1;

    private VocableListViewAdapter vocablesListViewAdapter;

    public VocablesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocables_list, container, false);
        vocablesListViewAdapter = new VocableListViewAdapter(getContext(), null);
        setListAdapter(vocablesListViewAdapter);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
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
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Cursor cursor = vocablesListViewAdapter.getCursor();
        eventBus.postSticky(new EventVocableSelected(getSelectedVocable(cursor, position)));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_dictionary_vocables_manager_long_press, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = contextMenuInfo.position;
        Cursor cursor = vocablesListViewAdapter.getCursor();

        switch (item.getItemId()) {
            case R.id.menu_dictionary_management_long_press_remove:
                eventBus.postSticky(
                        new EventVocableManipulationRequest(
                                getSelectedVocable(cursor, position), EventVocableManipulationRequest.TypeOfManipulation.REMOVE
                        ));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private Word getSelectedVocable(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        return DictionaryDAO.cursorToVocable(cursor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] columns = new String[]{VocablesContract.Schema.COLUMN_ID, VocablesContract.Schema.COLUMN_VOCABLE};
        return new CursorLoader(
                getActivity(),
                VocablesContract.CONTENT_URI,
                columns,
                null,
                null,
                VocablesContract.Schema.COLUMN_VOCABLE + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        vocablesListViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        vocablesListViewAdapter.swapCursor(null);
    }
}








