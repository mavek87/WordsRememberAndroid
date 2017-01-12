package com.matteoveroni.wordsremember.ui.items;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

public class WordsListViewAdapter extends CursorAdapter {

//    private SparseBooleanArray itemViewsSelectionArray = new SparseBooleanArray();

    public WordsListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new WordsListViewItem(context);
    }

//    /**
//     * Method to mark items in selection
//     */
//    public void setSelected(int position, boolean isSelected) {
//        itemViewsSelectionArray.put(position, isSelected);
//    }
//
//    public boolean isSelected(int position) {
//        return itemViewsSelectionArray.instance(position);
//    }
//
//    public void resetSelection() {
//        itemViewsSelectionArray.clear();
//    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
//        int position = cursor.getPosition();
//
//        if (itemViewsSelectionArray.instance(position)) {
//            itemView.setActivated(true);
//        } else {
//            itemView.setActivated(false);
//        }

        // Draw the cursor into the item view
        ((WordsListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(DictionaryContract.Schema.COLUMN_NAME)));
    }
}