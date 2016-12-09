package com.matteoveroni.wordsremember.dictionary.management;

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
import com.matteoveroni.wordsremember.events.EventManipulateVocable;
import com.matteoveroni.wordsremember.events.EventVocableSelected;
import com.matteoveroni.wordsremember.ui.items.WordsListViewAdapter;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

import org.greenrobot.eventbus.EventBus;

/**
 * List Fragment that shows all the vocables in the dictionary and allows to edit them
 * using a long press touch.
 *
 * @author Matteo Veroni
 */
public class DictionaryManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // ATTRIBUTES

    public static final String TAG = "F_dictionaryManagement";

    private WordsListViewAdapter dictionaryListViewAdapter;

    /**********************************************************************************************/

    // CONSTRUCTOR
    public DictionaryManagementFragment() {
    }

    /**********************************************************************************************/

    // ANDROID LIFECYCLE METHODS
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_management, container, false);
        getLoaderManager().initLoader(0, null, this);
        dictionaryListViewAdapter = new WordsListViewAdapter(getContext(), null);
        setListAdapter(dictionaryListViewAdapter);
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

    // ANDROID LIFECYCLE METHODS - LOADER MANAGER for CURSOR MANAGEMENT

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                DictionaryProvider.CONTENT_URI,
                DictionaryContract.Schema.ALL_COLUMNS,
                null,
                null,
                null
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

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    // ANDROID LIFECYCLE METHODS - MENU

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
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
//        if (id != lastSelectedVocableID) {
////                view.setActivated(true);
////            dictionaryListViewAdapter.setSelected(position, true);
//            lastSelectedVocableID = id;
//        } else {
////            view.setActivated(false);
////            dictionaryListViewAdapter.setSelected(position, false);
//            lastSelectedVocableID = -1;
//        }
        EventBus.getDefault().post(new EventVocableSelected(id));
    }

/**********************************************************************************************/
}








