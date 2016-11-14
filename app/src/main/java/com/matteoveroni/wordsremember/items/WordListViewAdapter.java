package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.database.tables.TableDictionary;

import java.util.List;

public class WordListViewAdapter extends CursorAdapter {

    public WordListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new WordListViewItem(context);
    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
        // TODO: set a different background color for odd and pair columns
//        if (cursor.getPosition() % 2 == 0) {
//            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
//        } else {
//            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
//        }
        ((WordListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(TableDictionary.COLUMN_WORD_NAME)));
    }
}