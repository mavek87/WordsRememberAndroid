package com.matteoveroni.wordsremember.dictionary.view;

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
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.ui.items.TranslationsListViewAdapter;
import com.matteoveroni.wordsremember.ui.items.VocableListViewAdapter;
import com.matteoveroni.wordsremember.utilities.Json;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

/**
 * @author Matteo Veroni
 */

public class TranslationsManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = TagGenerator.tag(TranslationsManagementFragment.class);

    private TranslationsListViewAdapter translationsListViewAdapter;
    private Word vocable;

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
        View view = inflater.inflate(R.layout.fragment_translations_management, container, false);
        getLoaderManager().initLoader(0, null, this);
        translationsListViewAdapter = new TranslationsListViewAdapter(getContext(), null);
        setListAdapter(translationsListViewAdapter);
        // Todo:
        // 1) get vocable from extra passed by dictionaryManipulationFragment
        // 2)
        ////////////////////////////////
//        vocable.setId(1);
        /////////////////////////////////
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
        // Todo: take bundle sent from DictionaryManipulationActivity
        //http://stackoverflow.com/questions/15392261/android-pass-dataextras-to-a-fragment
        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            String vocableInUse = bundle.getString("vocableInUse");
            vocable = Json.getInstance().fromJson(vocableInUse, Word.class);
        }
        registerForContextMenu(getListView());

    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = ((VocableListViewAdapter) listView.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);

        Toast.makeText(getContext(), selectedTranslation.getName(), Toast.LENGTH_SHORT).show();

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
        final String[] projection = new String[]{
                TranslationsContract.Schema.COLUMN_TRANSLATION
        };

        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.CONTENT_URI,
                projection,
                VocablesContract.Schema.COLUMN_ID,
                new String[]{"" + vocable.getId()},
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
