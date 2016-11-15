package com.matteoveroni.wordsremember.activities.dictionary_management;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.MenuItem;

import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.items.WordListViewAdapter;
import com.matteoveroni.wordsremember.model.Word;

import java.util.List;

public class DictionaryManagementFragment extends ListFragment {
//    public class DictionaryManagementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "DICTIONARY_MANAGEMENT_FRAGMENT";

    private WordListViewAdapter dictionaryListViewAdapter;
    private DictionaryDAO dictionaryDao;

    public DictionaryManagementFragment() {
        // Required empty public constructor
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        dictionaryDao = new DictionaryDAO(getContext());
//        dictionaryDao.openDbConnection();
//        return new CursorLoader(this, null, DictionaryContract.ALL_COLUMNS, null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        setupDictionaryListViewAdapter();
//        setListShown(true);
//        dictionaryDao.closeDbConnection();
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        setListShown(false);
//        setupDictionaryListViewAdapter();
//        setListShown(true);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dictionaryDao = new DictionaryDAO(getContext());

        dictionaryDao.saveVocable(new Word("impaurire"));

        List<Word> vocables = dictionaryDao.getAllVocablesList();

        dictionaryListViewAdapter = new WordListViewAdapter(getContext(), 1, vocables);

//        setListAdapter(new dictionaryListViewAdapter);

//        setListShown(false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
//        getLoaderManager().initLoader(0, null, this);

        registerForContextMenu(getListView());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupDictionaryListViewAdapter() {
//        dictionaryListViewAdapter = new WordListViewAdapter(getContext(), dictionaryDao.getAllVocables());
//        setListAdapter(dictionaryListViewAdapter);
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








