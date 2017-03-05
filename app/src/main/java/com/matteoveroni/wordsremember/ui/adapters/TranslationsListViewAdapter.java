package com.matteoveroni.wordsremember.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.ui.items.WordsListViewItem;

/**
 * @author Matteo Veroni
 */

public class TranslationsListViewAdapter extends CursorAdapter {

    public TranslationsListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new WordsListViewItem(context);
    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
        ((WordsListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(TranslationsContract.Schema.COLUMN_TRANSLATION)));
    }
}