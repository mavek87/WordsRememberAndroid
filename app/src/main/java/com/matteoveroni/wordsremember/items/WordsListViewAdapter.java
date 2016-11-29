package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;

public class WordsListViewAdapter extends CursorAdapter {

//    private SparseBooleanArray selectionArray = new SparseBooleanArray();

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
//        selectionArray.put(position, isSelected);
//    }
//
//    public boolean isSelected(int position) {
//        return selectionArray.get(position);
//    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
//        int position = cursor.getPosition();
//        Checkable checkableView = (Checkable) itemView;
//        checkableView.setChecked(isSelected(position));

        // Set the selecter for each itemView background dynamically
        // (instead of using xml setBackground property in the item view)
//        itemView.setBackgroundResource(R.drawable.list_item_selector);

        //      Use this to zebra columns
        //      itemView.setBackgroundResource(cursor.getPosition() % 2 == 0 ? R.drawable.list_item_pair_selector : R.drawable.list_item_odd_selector);

        // Draw the cursor into the item view
        ((WordsListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(DictionaryContract.Schema.COLUMN_NAME)));
    }
}