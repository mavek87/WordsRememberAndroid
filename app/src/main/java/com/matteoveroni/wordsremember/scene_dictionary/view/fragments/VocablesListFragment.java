package com.matteoveroni.wordsremember.scene_dictionary.view.fragments;

import android.database.Cursor;
import android.os.Bundle;
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
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.ui.listview.adapters.VocableListViewAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * Fragment that lists all the vocables in the dictionary using getSelectedTranslation Cursor Loader.
 * Is also possible to remove vocables using getSelectedTranslation long press touch.
 *
 * @author Matteo Veroni
 */

public class VocablesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final int ID_CURSOR_LOADER = 1;

    private LoaderManager fragLoaderManager;
    private VocableListViewAdapter vocableListAdapter;
    private ListView vocableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocables_list, container, false);

        fragLoaderManager = getLoaderManager();
        fragLoaderManager.initLoader(ID_CURSOR_LOADER, null, this);

        vocableListAdapter = new VocableListViewAdapter(getContext(), null);
        setListAdapter(vocableListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vocableListView = getListView();
        vocableListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onResume() {
        fragLoaderManager.restartLoader(ID_CURSOR_LOADER, null, this);
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor cursor = vocableListAdapter.getCursor();
        EVENT_BUS.post(new EventVocableSelected(getSelectedVocable(cursor, position)));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_dictionary_list_long_press, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(vocableListView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = contextMenuInfo.position;
        Cursor cursor = vocableListAdapter.getCursor();

        switch (item.getItemId()) {

            case R.id.menu_dictionary_list_long_press_remove:
                Word selectedVocable = getSelectedVocable(cursor, position);
                EVENT_BUS.post(
                        new EventVocableManipulationRequest(selectedVocable, TypeOfManipulationRequest.REMOVE)
                );
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
        final String[] columns = new String[]{VocablesContract.Schema.COL_ID, VocablesContract.Schema.COL_VOCABLE};
        return new CursorLoader(
                getActivity(),
                VocablesContract.CONTENT_URI,
                columns,
                null,
                null,
                VocablesContract.Schema.COL_VOCABLE + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        vocableListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        vocableListAdapter.swapCursor(null);
    }
}








