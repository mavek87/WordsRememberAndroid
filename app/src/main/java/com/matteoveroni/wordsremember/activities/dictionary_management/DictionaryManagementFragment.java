package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MenuItem;

import com.matteoveroni.wordsremember.items.WordListViewAdapter;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.WordsRememberContentProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.utilities.SimpleCursorLoader;

import java.util.List;

public class DictionaryManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "F_DICTIONARY_MANAGEMENT";

    private WordListViewAdapter dictionaryListViewAdapter;
    //    private SimpleCursorAdapter simpleCursorAdapter;
//    private DictionaryDAO dictionaryDao;

    public DictionaryManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        dictionaryDao = new DictionaryDAO(getContext());

        getLoaderManager().initLoader(0, null, this);

        setupDictionaryAdapter();

        setListShown(false);

        registerForContextMenu(getListView());
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
        setListShown(true);
        cursor.close();
//        dictionaryDao.closeDbConnection();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        setListShown(false);
        setupDictionaryAdapter();
        setListShown(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupDictionaryAdapter() {
//        dictionaryListViewAdapter = new WordListViewAdapter(getContext(), dictionaryDao.getAllVocables());
        setListAdapter(dictionaryListViewAdapter);

//        String[] fromFields = new String[]{DictionaryContract.Schema.COLUMN_NAME};
//        int[] toFields = new int[]{android.R.id.text1};
//
//        simpleCursorAdapter = new SimpleCursorAdapter(
//                getActivity(),
//                android.R.layout.simple_expandable_list_item_1,
//                null,
//                fromFields,
//                toFields,
//                0
//        );
    }


//    private void loadDictionaryProgrammatically(){
//        addWordImpaurireToDictionary();
//        addWordCaneToDictionary();
//    }

//    private void addWordImpaurireToDictionary() {
//        Word wordImpaurire = new Word("impaurire");
//        Word wordFrighten = new Word("frighten");
//        Word wordScare = new Word("scare");
//
//        List<Word> translationsForWordImpaurire = new ArrayList<>();
//        translationsForWordImpaurire.add(wordScare);
//        translationsForWordImpaurire.add(wordFrighten);
//        dictionary.addTranslationsForVocable(translationsForWordImpaurire, wordImpaurire);
//    }
//
//    private void addWordCaneToDictionary() {
//        Word wordCane = new Word("cane");
//        Word wordDog = new Word("dog");
//
//        List<Word> translationsForWordCane = new ArrayList<>();
//        translationsForWordCane.add(wordDog);
//        dictionary.addTranslationsForVocable(translationsForWordCane, wordCane);
//
//
//        dictionary.removeVocable(wordDog);
//    }

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








