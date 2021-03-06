package com.matteoveroni.wordsremember.ui.listview.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.ui.listview.items.ListViewItem;

/**
 * @author Matteo Veroni
 */

public class VocableListViewAdapter extends CursorAdapter {

//    private SparseBooleanArray itemViewsSelectionArray = new SparseBooleanArray();

    public VocableListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new ListViewItem(context);
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
        ((ListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(VocablesContract.Schema.COL_VOCABLE)));
    }
}