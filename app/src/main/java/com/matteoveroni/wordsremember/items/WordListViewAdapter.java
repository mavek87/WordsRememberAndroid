package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

import java.util.List;

public class WordListViewAdapter extends CursorAdapter {

    private SparseBooleanArray selectionArray = new SparseBooleanArray();

    public WordListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new WordListViewItem(context);
    }

    /**
     * Method to mark items in selection
     */
    public void setSelected(int position, boolean isSelected) {
        selectionArray.put(position, isSelected);
    }

    public boolean isSelected(int position) {
        return selectionArray.get(position);
    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
        int position = cursor.getPosition();
        Checkable checkableView = (Checkable) itemView;
        checkableView.setChecked(isSelected(position));

        // TODO: set a different background color for odd and pair columns
//        itemView.setBackgroundResource(cursor.getPosition() % 2 == 0 ? R.drawable.list_selector_first : R.drawable.list_selector_second);
        ((WordListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(DictionaryContract.Schema.COLUMN_NAME)));
    }

}