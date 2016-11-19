package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.items.WordListViewAdapter;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

/**
 * List Fragment that shows all the vocables in the dictionary and allows to manage the
 * dictionary adding, editing or removing new vocables.
 *
 * @author Matteo Veroni
 */
public class DictionaryManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "F_Dictionary_Management";

    private WordListViewAdapter dictionaryListViewAdapter;

    public DictionaryManagementFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment DictionaryManagementFragment.
     */
    public static DictionaryManagementFragment getInstance() {
        return new DictionaryManagementFragment();
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

    private void setupDictionaryAdapter() {
        dictionaryListViewAdapter = new WordListViewAdapter(getContext(), null);
        setListAdapter(dictionaryListViewAdapter);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater menuInflater = getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.long_press_menu, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int position = contextMenuInfo.position;
//
//        switch (item.getItemId()) {
//            case R.id.edit:
//                launchNoteDetailActivity(FragmentToLaunch.EDIT, position);
//                return true;
//        }
//        return super.onContextItemSelected(item);
//    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        launchNoteDetailActivity(FragmentToLaunch.VIEW, position);
//    }
//
//    private void launchNoteDetailActivity(FragmentToLaunch fragment, int position) {
//        Note selectedNote = (Note) getListAdapter().getItem(position);
//
//        Intent launchNodeDetail = new Intent(getContext(), NoteDetailActivity.class);
//        launchNodeDetail.putExtra(NOTE_KEY, selectedNote.toString());
//
//        switch (fragment) {
//            case VIEW:
//                launchNodeDetail.putExtra(NOTE_DETAIL_FRAGMENT_TO_START_KEY, FragmentToLaunch.VIEW);
//                break;
//            case EDIT:
//                launchNodeDetail.putExtra(NOTE_DETAIL_FRAGMENT_TO_START_KEY, FragmentToLaunch.EDIT);
//                break;
//        }
//        startActivity(launchNodeDetail);
//    }
}








