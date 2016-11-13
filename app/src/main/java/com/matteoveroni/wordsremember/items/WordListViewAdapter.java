package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.matteoveroni.wordsremember.model.Word;

import java.util.List;

public class WordListViewAdapter extends ArrayAdapter<Word> {

    public WordListViewAdapter(Context context, List<Word> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = new WordListView(parent.getContext());
        }
        ((WordListView) view).draw(getItem(position));
        return view;
    }

}