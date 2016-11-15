package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.matteoveroni.wordsremember.model.Word;

import java.util.List;

public class WordListViewAdapter extends ArrayAdapter<Word> {
    //    public class WordListViewAdapter extends CursorAdapter {
//    public WordListViewAdapter(Context context, Cursor cursor) {
//        super(context, cursor, 0);
//    }

    public WordListViewAdapter(Context context, int resource, List<Word> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return new WordListViewItem(getContext());
    }

    //    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        return new WordListViewItem(context);
//    }

//    @Override
//    public void bindView(View itemView, Context context, Cursor cursor) {
//        // TODO: set a different background color for odd and pair columns
////        if (cursor.getPosition() % 2 == 0) {
////            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
////        } else {
////            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
////        }
//        ((WordListViewItem) itemView).draw(cursor.getString(cursor.getColumnIndexOrThrow(DictionaryContract.COLUMN_WORD_NAME)));
//    }
}