package com.matteoveroni.wordsremember.dictionary.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.async_commands.AsyncInsertCommand;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.ui.items.WordsListViewAdapter;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

/**
 * @author Matteo Veroni
 */

public class TranslationsManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = TagGenerator.tag(TranslationsManagementFragment.class);

    private WordsListViewAdapter translationsListViewAdapter;

    /**********************************************************************************************/

    // Android lifecycle methods

    /**********************************************************************************************/

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(0, null, this);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary_management, container, false);
        getLoaderManager().initLoader(0, null, this);
        translationsListViewAdapter = new WordsListViewAdapter(getContext(), null);
        setListAdapter(translationsListViewAdapter);

        ContentValues v = new ContentValues();
        v.put(TranslationsContract.Schema.COLUMN_TRANSLATION, "ciaone");
        new AsyncInsertCommand(getContext().getContentResolver(), TranslationsContract.CONTENT_URI, v).execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity().getApplicationContext(), TAG, Toast.LENGTH_SHORT).show();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = ((WordsListViewAdapter) listView.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);

        Toast.makeText(getActivity().getApplicationContext(), selectedTranslation.getName(), Toast.LENGTH_SHORT).show();

//        eventBus.postSticky(new EventVocableSelected(selectedVocable));
    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater menuInflater = getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.menu_dictionary_management_long_press, menu);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        int position = contextMenuInfo.position;
//
//        Cursor cursor = dictionaryListViewAdapter.getCursor();
//        cursor.moveToPosition(position);
//
//        Word selectedVocable = DictionaryDAO.cursorToVocable(cursor);
//
//        switch (item.getItemId()) {
//            case R.id.menu_dictionary_management_long_press_remove:
//                eventBus.postSticky(new EventVocableManipulationRequest(selectedVocable, EventVocableManipulationRequest.TypeOfManipulation.REMOVE));
//                return true;
//        }
//        return super.onContextItemSelected(item);
//    }

    /**********************************************************************************************/

    // LoaderManager callbacks

    /**********************************************************************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                TranslationsContract.CONTENT_URI,
                TranslationsContract.Schema.ALL_COLUMNS,
                null,
                null,
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
