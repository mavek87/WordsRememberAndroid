package com.matteoveroni.wordsremember.ui.listview.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.ui.listview.items.ListViewItem;

/**
 * @author Matteo Veroni
 */

public class UserProfilesListViewAdapter extends CursorAdapter {

    public UserProfilesListViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new ListViewItem(context);
    }

    @Override
    public void bindView(View itemView, Context context, Cursor cursor) {
        ((ListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(UserProfilesContract.Schema.COL_PROFILE_NAME)));
    }
}