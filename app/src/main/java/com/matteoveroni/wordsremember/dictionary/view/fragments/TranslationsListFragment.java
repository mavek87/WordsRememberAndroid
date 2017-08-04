package com.matteoveroni.wordsremember.dictionary.view.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationSelected;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.PojoManipulable;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.ui.listview.adapters.TranslationsListViewAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Matteo Veroni
 */

public class TranslationsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, PojoManipulable<Word> {

    public static final String TAG = TagGenerator.tag(TranslationsListFragment.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    public enum Type {
        TRANSLATIONS, TRANSLATIONS_FOR_VOCABLE, TRANSLATIONS_NOT_FOR_VOCABLE;
    }

    public Type type = Type.TRANSLATIONS;

    private Unbinder viewInjector;
    private TranslationsListViewAdapter adapter_translationsList;
    private Word vocableAssociatedToView;

    @BindView(R.id.fragment_translations_list_title)
    TextView lbl_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_translations_list, container, false);
        viewInjector = ButterKnife.bind(this, view);
        adapter_translationsList = new TranslationsListViewAdapter(getContext(), null);
        setListAdapter(adapter_translationsList);
        return view;
    }

    @Override
    public void onDestroyView() {
        viewInjector.unbind();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onResume() {
        int CURSOR_LOADER_ID = 1;
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, getArguments(), this);
        super.onResume();
    }

    @Override
    public Word getPojoUsed() {
        return vocableAssociatedToView;
    }

    @Override
    public void setPojoUsed(Word vocable) {
        this.vocableAssociatedToView = vocable;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Cursor cursor = adapter_translationsList.getCursor();
        cursor.moveToPosition(position);

        Word selectedTranslation = DictionaryDAO.cursorToTranslation(cursor);
        EventBus.getDefault().post(new EventTranslationSelected(selectedTranslation));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (type) {
            case TRANSLATIONS:
                lbl_title.setText("Translations");
                return getCursorForAllTheTranslations();
            case TRANSLATIONS_FOR_VOCABLE:
                lbl_title.setText("Translations for vocable");
                return getCursorForAllTheTranslationsForVocable();
            case TRANSLATIONS_NOT_FOR_VOCABLE:
                lbl_title.setText("Other translations available");
                return getCursorForAllTheTranslationsExceptThoseForVocable();
            default:
                final String error = "Error during onCreateLoader. Unknown type of fragment set.";
                Log.e(TAG, error);
                throw new RuntimeException(error);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter_translationsList.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter_translationsList.swapCursor(null);
    }

    private Loader<Cursor> getCursorForAllTheTranslations() {
        return new CursorLoader(
                getContext(),
                TranslationsContract.CONTENT_URI,
                null,
                null,
                null,
                TranslationsContract.Schema.COL_TRANSLATION + " ASC");
    }

    private Loader<Cursor> getCursorForAllTheTranslationsForVocable() {
        return new CursorLoader(
                getContext(),
                VocablesTranslationsContract.TRANSLATIONS_FOR_VOCABLE_CONTENT_URI,
                null,
                null,
                new String[]{String.valueOf(vocableAssociatedToView.getId())},
                TranslationsContract.Schema.COL_TRANSLATION + " ASC");

    }

    private Loader<Cursor> getCursorForAllTheTranslationsExceptThoseForVocable() {
        String vocableId = String.valueOf(vocableAssociatedToView.getId());
        Uri uri = Uri.withAppendedPath(VocablesTranslationsContract.NOT_TRANSLATION_FOR_VOCABLE_CONTENT_URI, vocableId);
        return new CursorLoader(
                getContext(),
                uri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (type != Type.TRANSLATIONS_NOT_FOR_VOCABLE) {
            allowOperationsMenu(menu);
        }
    }

    private void allowOperationsMenu(ContextMenu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_dictionary_list_long_press, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = contextMenuInfo.position;
        final Cursor cursor = adapter_translationsList.getCursor();
        switch (item.getItemId()) {
            case R.id.menu_dictionary_list_long_press_remove:
                Word selectedTranslation = getSelectedTranslation(cursor, position);
                removeTranslationRequest(selectedTranslation);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private Word getSelectedTranslation(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        return DictionaryDAO.cursorToTranslation(cursor);
    }

    private void removeTranslationRequest(Word translation) {
        switch (type) {
            case TRANSLATIONS:
                EVENT_BUS.post(new EventTranslationManipulationRequest(translation, TypeOfManipulationRequest.REMOVE));
                break;
            case TRANSLATIONS_FOR_VOCABLE:
                EVENT_BUS.post(new EventVocableTranslationManipulationRequest(vocableAssociatedToView.getId(), translation.getId(), TypeOfManipulationRequest.REMOVE));
                break;
            default:
                throw new RuntimeException("Unexpected type");
        }
    }
}
