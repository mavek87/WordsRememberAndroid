package com.matteoveroni.wordsremember.dictionary;

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

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.ui.items.TranslationsListViewAdapter;
import com.matteoveroni.wordsremember.ui.items.VocableListViewAdapter;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class TranslationsManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = TagGenerator.tag(TranslationsManagementFragment.class);

    private final int CURSOR_LOADER_ID = 1;

    private TranslationsListViewAdapter translationsListViewAdapter;
    private Word selectedVocable;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Android lifecycle methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Creating the view");

        View view = inflater.inflate(R.layout.fragment_translations_management, container, false);
        translationsListViewAdapter = new TranslationsListViewAdapter(getContext(), null);
        setListAdapter(translationsListViewAdapter);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

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
        Log.i(TAG, "View created");

        Toast.makeText(getActivity().getApplicationContext(), TAG, Toast.LENGTH_SHORT).show();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "Activity created");

        // Todo: take bundle sent from DictionaryManipulationActivity
        //http://stackoverflow.com/questions/15392261/android-pass-dataextras-to-a-fragment
//        final Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            String vocableInUse = bundle.getString("vocableInUse");
//            vocable = Json.getInstance().fromJson(vocableInUse, Word.class);
//        }
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

        Cursor cursor = ((VocableListViewAdapter) listView.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);

        Toast.makeText(getContext(), selectedTranslation.getName(), Toast.LENGTH_SHORT).show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LoaderManager callbacks
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating loader");

        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.CONTENT_URI,
                null,
                null,
                new String[]{"" + 1},
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isFragmentCreated() {
        return getView() != null;
    }
}
